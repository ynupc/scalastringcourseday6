package text.search

import scala.collection.mutable.ListBuffer

/**
  * @author ynupc
  *         Created on 2016/08/20
  */
object MorrisPratt extends Search {
  override def indexOf[T](source: Array[T], target: Array[T]): Int = {
    var i: Int = 0
    var j: Int = 0

    val mp: Seq[Int] = preProcess(source)

    while (j < target.length) {
      while (-1 < i && source(i) != target(j)) {
        i = mp(i)
      }
      i += 1
      j += 1
      if (source.length <= i) {
        return j - i
      }
    }

    -1
  }

  def indicesOf[T](source: Array[T], target: Array[T]): Array[Int] = {
    var i: Int = 0
    var j: Int = 0

    val mp: Seq[Int] = preProcess(source)
    val ret: ListBuffer[Int] = ListBuffer[Int]()

    while (j < target.length) {
      while (-1 < i && source(i) != target(j)) {
        i = mp(i)
      }
      i += 1
      j += 1
      if (source.length <= i) {
        ret += j - i
        i = mp(i)
      }
    }

    ret.toArray
  }

  private def preProcess[T](source: Array[T]): Seq[Int] = {
    var i: Int = 0
    var j: Int = -1
    val mp: ListBuffer[Int] = ListBuffer[Int]()
    mp += -1

    while (i < source.length) {
      while (-1 < j && source(i) != source(j)) {
        j = mp(j)
      }
      i += 1
      j += 1
      mp(i) = j
    }

    mp.result
  }
}
