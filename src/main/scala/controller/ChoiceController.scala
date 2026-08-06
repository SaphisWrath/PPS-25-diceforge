package controller

import controller.dto.PlayerDTO

type PlayerChoice[A] = (PlayerDTO, Seq[A])
object PlayerChoice:
  def apply[A](player: PlayerDTO, options: Seq[A]): PlayerChoice[A] = (player, options)

trait ChoiceController[A]:
  def pendingChoices: Seq[PlayerChoice[A]]
  def resumeAfterChoices(results: Seq[(PlayerDTO, A)]): Unit
