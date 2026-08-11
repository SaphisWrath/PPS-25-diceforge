package controller.choices

import controller.dto.PlayerDTO

type PlayerChoice[A] = (PlayerDTO, Seq[A])
object PlayerChoice:
  def apply[A](player: PlayerDTO, options: Seq[A]): PlayerChoice[A] = (player, options)

/**
 * A controller to manage long operations that require user input
 * @tparam A the type of the objects the player is choosing from
 */
trait ChoiceController[A]:
  /**
   *
   * @return the sequence of choices the user must answer before the next phase of the operation
   */
  def pendingChoices: Seq[PlayerChoice[A]]

  /**
   * Continues the previously stopped operation
   * @param results from the user's choices, used for the following phase of the operation
   */
  def resumeAfterChoices(results: Seq[Int]): Unit
