package utils

object SeqUtils:
  extension [A](seq: Seq[A])
    def rotate(n: Int): Seq[A] = seq match
      case Nil => Nil
      case _ => seq.drop(n) ++ seq.take(n)
