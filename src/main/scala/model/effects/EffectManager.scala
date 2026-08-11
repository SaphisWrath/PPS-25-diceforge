package model.effects

import model.ModelPublisher
import model.ModelPublisher.ModelContext.{DiceThrownContext, EffectChoiceContext, ResourceContext}
import model.Players.Player
import model.effects.*
import model.utils.ResourceEffectModule
import model.utils.ResourceEffectModules.AddResource

import scala.annotation.tailrec

/**
 * A manager that attempts to solve every effect given
 * Stores partial results when it needs user input, then attempts to solve again with the new info
 */
trait EffectManager:
  /**
   * Tries to solve all effects and succeeds immediately when no user input is required
   * @param effects the effects it tries to resolve
   * @param updateTurnEffects whether all the saved effects should be replaced by the incoming effects or not
   */
  def attemptSolve(effects: Seq[(Player, Effect)], updateTurnEffects: Boolean = false): Unit

  /**
   *
   * @return the effects that require user input to solve further
   */
  def effectsToSolve: Seq[(Player, OptionEffect)]

  /**
   * Sets the current turn's effects rolled by the player's dice
   * @param turnEffects the newly rolled effects that must replace the older ones
   */
  def updateTurnEffects(turnEffects: Seq[(Player, Effect, Int)]): Unit

  /**
   * Sets a specific ResourceEffectModule to apply on the next solve
   * It resets to default when the next solve is completed
   * @param module the module to use for next solve
   */
  def setModuleOnce(module: ResourceEffectModule): Unit

object EffectManager:
  private class EffectManagerImpl extends EffectManager:
    private var effectCache: Seq[(Player, Effect)] = Seq.empty
    private var _effectsToSolve: Seq[(Player, OptionEffect)] = Seq.empty
    private var _currentTurnEffects: Seq[(Player, Effect)] = Seq.empty
    private var _module: ResourceEffectModule = AddResource

    override def effectsToSolve: Seq[(Player, OptionEffect)] = _effectsToSolve

    override def updateTurnEffects(newEffects: Seq[(Player, Effect, Int)]): Unit =
      ModelPublisher().notify(DiceThrownContext)
      if _currentTurnEffects.isEmpty
      then _currentTurnEffects = newEffects.map((p, e, _) => (p, e))
      else
        var count: Int = 0
        var lastPlayer = _currentTurnEffects.head._1
        _currentTurnEffects = _currentTurnEffects
          .map((p, e) =>
            if lastPlayer.name != p.name
            then
              count = 0
              lastPlayer = p
            count = count + 1
            (p, e, count - 1)
          ).map((p, e, i) => newEffects.find((_p, _, _i) => p.name == _p.name && i == _i).getOrElse((p, e, i)))
          .map((p, e, _) => (p, e))

    override def attemptSolve(effects: Seq[(Player, Effect)], updateTurnEffects: Boolean): Unit =
      if updateTurnEffects
      then
        _currentTurnEffects = effects
        ModelPublisher().notify(DiceThrownContext)
      val (copyEffects, otherEffects) = splitCopyEffects(flattenSumEffects(effects.concat(effectCache)))
      if copyEffects.nonEmpty
        then
          effectCache = otherEffects
          _effectsToSolve = copyEffects
            .map((p, ce) => (p, OptionEffect(_currentTurnEffects.flatMap((otherP, otherE) =>
              if otherP.name == p.name then Seq.empty else Seq(otherE)
            ))))
          ModelPublisher().notify(EffectChoiceContext)
      else
        val (optionEffects, nonOptionEffects) = getOptionEffects(otherEffects)
        if optionEffects.nonEmpty
          then
            effectCache = nonOptionEffects
            _effectsToSolve = optionEffects
            ModelPublisher().notify(EffectChoiceContext)
        else
          effectCache = Seq.empty
          resolveAll(nonOptionEffects)
          _module = AddResource
          ModelPublisher().notify(ResourceContext)

    override def setModuleOnce(module: ResourceEffectModule): Unit = _module = module

    @tailrec
    private def flattenSumEffects(effects: Seq[(Player, Effect)]): Seq[(Player, Effect)] =
      val currentRound = effects.flatMap((p, e) => e match
        case e: SumEffect => e.effects.map(e => (p, e))
        case _ => Seq((p, e))
      )

      if !currentRound.exists((_, e) => e match {
        case effect: SumEffect => true
        case _ => false
      }) then currentRound else flattenSumEffects(currentRound)

    private def splitCopyEffects(effects: Seq[(Player, Effect)]):
      (Seq[(Player, CopyEffect)], Seq[(Player, Effect)]) =
      val (copyEffects, resourceEffects) = effects
        .partition((_, e) => e.isInstanceOf[CopyEffect])
      (copyEffects.map((p, e) => (p, e.asInstanceOf[CopyEffect])), resourceEffects)

    private def getOptionEffects(effects: Seq[(Player, Effect)]):
    (Seq[(Player, OptionEffect)], Seq[(Player, Effect)]) =
      val (optionEffects, resourceEffects) = effects
        .partition((_, e) => e.isInstanceOf[OptionEffect])
      (optionEffects.map((p, e) => (p, e.asInstanceOf[OptionEffect])), resourceEffects)

    private def resolveAll(effects: Seq[(Player, Effect)]): Unit =
      val (multiplyEffects, otherEffects) = effects
        .partition((_, e) => e.isInstanceOf[MultiplyEffect])

      val resourceEffects = otherEffects
        .flatMap((p, e) => e match {
          case effect: ResourceEffect => Seq((p, effect))
          case _ => Seq.empty
        })
      resourceEffects.foreach((p, e) =>
          e.setModule(_module)
          e.resolve(p)
      )
      multiplyEffects
        .map((p, e) => (p, e.asInstanceOf[MultiplyEffect]))
        .foreach((p, e) =>
          e.currentEffect = SumEffect(
            resourceEffects
              .filter((player, _) => player.name == p.name)
              .map(_._2))
          e.resolve(p)
        )

  private val effectManager = EffectManagerImpl()    
  
  def apply(): EffectManager = effectManager