package controller

import controller.ViewPublisher.ViewContext.PlayerChoiceContext
import controller.dto.{CompoundEffectDTO, DieDTO, EffectDTO, PlayerDTO}
import model.GameMatch
import model.Players.Player
import model.dice.Die
import model.effects.{Effect, EffectWrapper, ResourceEffect}
import model.utils.DiceThrow

type PlayerChoice[A] = (PlayerDTO, Seq[A])
object PlayerChoice:
  def apply[A](player: PlayerDTO, options: Seq[A]): PlayerChoice[A] = (player, options)

trait DiceThrowManager:
  def copyEffectsFromRoll(dice: Seq[(Player, Seq[Die])]): Seq[PlayerChoice[EffectDTO]]
  def optionEffectsFromRoll(solvedCopyEffects: Seq[(PlayerDTO, EffectDTO)]): Seq[PlayerChoice[EffectDTO]]
  def endRoll(solvedOptionEffects: Seq[(PlayerDTO, EffectDTO)]): Unit
  def allRawEffects: Seq[(PlayerDTO, EffectDTO)]

object DiceThrowManager:
  private def asPlayer(gameMatch: GameMatch)(playerDTO: PlayerDTO): Player =
    gameMatch.players.find(p => p.name == playerDTO.name).get
    
  private def asEffect(effectsSource: Seq[Effect])(effectDTO: EffectDTO): Effect =
    def equalEffects(e1: EffectDTO, e2: EffectDTO): Boolean =
      def equalOptions[A](o1: Option[A], o2: Option[A]): Boolean =
        (o1.isEmpty && o2.isEmpty) || (o1.isDefined && o2.isDefined && o1.contains(o2.get))
      e1.sprite == e2.sprite && equalOptions(e1.label, e2.label)

    effectsSource.find(effect => (EffectDTO(effect), effectDTO) match
      case (CompoundEffectDTO(effects_1), CompoundEffectDTO(effects_2)) =>
        effects_1.zip(effects_2).forall((e1, e2) => equalEffects(e1, e2))
      case (e1: EffectDTO, e2: EffectDTO) => equalEffects(e1,e2)
    ).get

  private class DiceThrowManagerImpl(gameMatch: GameMatch) extends DiceThrowManager:
    private val diceThrowHelper = DiceThrow(gameMatch)
    private val toPlayer: PlayerDTO => Player = asPlayer(gameMatch)
    private var effectsInLastChoice: Seq[Effect] = Seq.empty
    var allRawEffects: Seq[(PlayerDTO, EffectDTO)] = Seq.empty

    override def copyEffectsFromRoll(dice: Seq[(Player, Seq[Die])]): Seq[PlayerChoice[EffectDTO]] =
      val (copyEffects, otherEffects) = diceThrowHelper.initiateDiceRoll(dice)
      allRawEffects = copyEffects.concat(otherEffects).map((p, e) => (PlayerDTO(p), EffectDTO(e)))
      effectsInLastChoice = otherEffects.map(_._2)
      copyEffects.map((p, e) =>
        PlayerChoice(PlayerDTO(p), otherEffects.flatMap((otherP, otherE) => if otherP == p then Seq.empty else Seq(EffectDTO(otherE))))
      )

    override def optionEffectsFromRoll(solvedCopyEffects: Seq[(PlayerDTO, EffectDTO)]): Seq[PlayerChoice[EffectDTO]] =
      val optionEffects = diceThrowHelper
        .sortEffects(solvedCopyEffects.map((p, e) => (toPlayer(p), asEffect(effectsInLastChoice)(e))))
      effectsInLastChoice = optionEffects.flatMap(_._2.options)
      optionEffects.map((p, e) => PlayerChoice(PlayerDTO(p), e.options.map(EffectDTO(_))))

    override def endRoll(solvedOptionEffects: Seq[(PlayerDTO, EffectDTO)]): Unit =
      diceThrowHelper.resolveAll(solvedOptionEffects.map((p, e) => (toPlayer(p), asEffect(effectsInLastChoice)(e))))

  def apply(gameMatch: GameMatch): DiceThrowManager = DiceThrowManagerImpl(gameMatch)