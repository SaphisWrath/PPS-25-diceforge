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
   */
  def attemptSolve(effects: Seq[(Player, Effect)]): Unit

  /**
   *
   * @return the effects that require user input to solve further
   */
  def effectsToSolve: Seq[(Player, OptionEffect)]

object EffectManager:
  private class EffectManagerImpl extends EffectManager:
    private var effectCache: Seq[(Player, Effect)] = Seq.empty
    private var _effectsToSolve: Seq[(Player, OptionEffect)] = Seq.empty
    
    override def effectsToSolve: Seq[(Player, OptionEffect)] = _effectsToSolve
    
    override def attemptSolve(effects: Seq[(Player, Effect)]): Unit =
      val (copyEffects, otherEffects) = splitCopyEffects(effects.concat(effectCache))
      if copyEffects.nonEmpty
        then
          effectCache = otherEffects
          _effectsToSolve = copyEffects
            .map((p, ce) => (p, OptionEffect(otherEffects.flatMap((otherP, otherE) =>
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
      ModelPublisher().notify(ResourceContext)
      
  private val effectManager = EffectManagerImpl()    
  
  def apply(): EffectManager = effectManager