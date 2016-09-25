package text.search

import scala.collection.mutable
import mutable.ListBuffer

/**
  * @author ynupc
  *         Created on 2016/08/20
  */
object ShiftAnd extends Search {
  override def indexOf[T](source: Array[T], target: Array[T]): Int = {
    val (finish, mask): (Long, Map[T, Int]) = preProcess[T](source, target)

    var state: Int = 0
    for (i <- source.indices) {
      state = and[T](state, mask(source(i)))
      if (finish <= state) {
        return i
      }
    }

    -1
  }

  override def indicesOf[T](source: Array[T], target: Array[T]): Array[Int] = {
    val (finish, mask): (Long, Map[T, Int]) = preProcess[T](source, target)

    var state: Int = 0
    val ret: ListBuffer[Int] = ListBuffer[Int]()
    for (i <- source.indices) {
      state = and[T](state, mask(source(i)))
      if (finish <= state) {
        ret += i
      }
    }

    ret.toArray
  }

  private def initialValue[T](target: Array[T]): Long = {
    1L << target.length - 1
  }

  private def and[T](state: Int, mask: Int): Int = {
    (state << 1|1) & mask
  }

  private def preProcess[T](source: Array[T], target: Array[T]): (Long, Map[T, Int]) = {
    val charTable: ListBuffer[T] = ListBuffer[T]()
    source foreach {
      case char if !charTable.contains(char) =>
        charTable += char
      case otherwise =>
        // Do nothing
    }
    var finish: Long = initialValue[T](target)

    val mask: mutable.Map[T, Int] = mutable.Map[T, Int]()
    charTable foreach {
      char =>
        mask(char) = 0
    }

    target foreach {
      char =>
        mask.keys foreach {
          key =>
            mask(key) >>= 1
            if (char == key) {
//              mask(key) |= finish
            }
        }
    }

    (finish, mask.toMap)
  }
}
