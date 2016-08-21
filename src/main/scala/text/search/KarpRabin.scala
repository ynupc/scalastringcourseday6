package text.search

import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks

/**
  * @author ynupc
  *         Created on 2016/08/20
  */
object KarpRabin extends Search {
  override def indexOf[T](source: Array[T], target: Array[T]): Int = {
    val targetLength: Int = target.length
    val targetHash: Int = rollingHash(target, 0, targetLength, 0)
    var sourceHash: Int = 0

    for (i <- 0 to source.length - targetLength) {
      sourceHash = rollingHash(source, i, targetLength, sourceHash)
      if (sourceHash == targetHash) {
        var isOkay: Boolean = true
        var k: Int = i
        var j: Int = 0
        val b: Breaks = new Breaks()
        b.breakable {
          while (k < targetLength && isOkay) {
            k += 1
            j += 1
            if (source(i) != target(j)) {
              isOkay = false
              b.break
            }
          }
        }
        if (isOkay) {
          return i
        }
      }
    }
    -1
  }

  override def indicesOf[T](source: Array[T], target: Array[T]): Array[Int] = {
    val targetLength: Int = target.length
    val targetHash: Int = rollingHash(target, 0, targetLength, 0)
    var sourceHash: Int = 0
    val ret: ListBuffer[Int] = ListBuffer[Int]()

    for (i <- 0 to source.length - targetLength) {
      sourceHash = rollingHash(source, i, targetLength, sourceHash)
      if (sourceHash == targetHash) {
        var isOkay: Boolean = true
        var k: Int = i
        var j: Int = 0
        val b: Breaks = new Breaks()
        b.breakable {
          while (k < targetLength && isOkay) {
            k += 1
            j += 1
            if (source(i) != target(j)) {
              isOkay = false
              b.break
            }
          }
        }
        if (isOkay) {
          ret += i
        }
      }
    }

    ret.toArray
  }

  private def rollingHash[T](string: Array[T], i: Int, length: Int, hval: Int): Int = {
    var hval_ : Long = hval

    if (i == 0) {
      hval_ = 0
      var k: Int = 0
      for (k <- 0 until length) {
        hval_ += toInt(string(k))
      }
    }

    if (!(hval_ == 0 || i ==0)) {
      hval_ -= toInt(string(i - 1))
    }

    if (i + length <= string.length) {
      hval_ += toInt(string(i + length - 1))
    } else {
      return -1
    }

    hval
  }

  private def toInt[T](element: T): Long = {
    element match {
      case e: Byte =>
        e.toLong
      case e: Short =>
        e.toLong
      case e: Int =>
        e.toLong
      case e: Long =>
        e
      case otherwise =>
        throw new Exception()
    }
  }
}
