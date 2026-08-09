package model.utils

import model.ModelPublisher.ModelContext.{ChoiceContext, ResourceContext}
import model.ModelPublisher
import model.Players.Player
import model.effects.*

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

object EffectManager:
  private class EffectManagerImpl extends EffectManager:
    private var effectCache: Seq[(Player, Effect)] = Seq.empty
    private var _effectsToSolve: Seq[(Player, OptionEffect)] = Seq.empty
    private var _currentTurnEffects: Seq[(Player, Effect)] = Seq.empty

    override def effectsToSolve: Seq[(Player, OptionEffect)] = _effectsToSolve

    override def updateTurnEffects(newEffects: Seq[(Player, Effect, Int)]): Unit =
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
        .map((p, e, i) => (p, e))

    override def attemptSolve(effects: Seq[(Player, Effect)], updateTurnEffects: Boolean): Unit =
      if updateTurnEffects then _currentTurnEffects = effects
      val (copyEffects, otherEffects) = splitCopyEffects(effects.concat(effectCache))
      if copyEffects.nonEmpty
        then
          effectCache = otherEffects
          _effectsToSolve = copyEffects
            .map((p, ce) => (p, OptionEffect(_currentTurnEffects.flatMap((otherP, otherE) =>
              if otherP.name == p.name then Seq.empty else Seq(otherE)
            ))))
          ModelPublisher().notify(ChoiceContext)
      else
        val (optionEffects, nonOptionEffects) = getOptionEffects(otherEffects)
        if optionEffects.nonEmpty
          then
            effectCache = nonOptionEffects
            _effectsToSolve = optionEffects
            ModelPublisher().notify(ChoiceContext)
        else
          effectCache = Seq.empty
          resolveAll(nonOptionEffects)
          ModelPublisher().notify(ResourceContext)

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
      val (multiplyEffects, resourceEffects) = effects
        .partition((_, e) => e.isInstanceOf[MultiplyEffect])

      resourceEffects.foreach((p, e) => e.resolve(p))
      multiplyEffects
        .map((p, e) => (p, e.asInstanceOf[MultiplyEffect]))
        .foreach((p, e) =>
          resourceEffects
            .find((player, _) => player.name == p.name)
            .map(_._2) match
            case Some(effect) => e.currentEffect = effect
            case _ =>
          e.resolve(p)
        )
      
  private val effectManager = EffectManagerImpl()    
  
  def apply(): EffectManager = effectManager