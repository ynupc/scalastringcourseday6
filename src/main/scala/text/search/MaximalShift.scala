package text.search

/**
  * @author ynupc
  *         Created on 2016/08/21
  */
object MaximalShift extends Search {
  override def indexOf[T](source: Array[T], target: Array[T]): Int = {
    -1
  }

  override def indicesOf[T](source: Array[T], target: Array[T]): Array[Int] = {
    Array()
  }
}
