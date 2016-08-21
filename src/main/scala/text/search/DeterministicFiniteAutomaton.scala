package text.search

/**
  * @author ynupc
  *         Created on 2016/08/20
  */
object DeterministicFiniteAutomaton extends Search {
  override def indexOf[T](source: Array[T], target: Array[T]): Int = {
    0
  }

  override def indicesOf[T](source: Array[T], target: Array[T]): Array[Int] = {
    Array()
  }
}
