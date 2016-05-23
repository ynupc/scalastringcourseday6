package text.vector

import scala.collection.mutable

/**
  * @author ynupc
  *         Created on 2016/05/22
  */
class BinaryVector(var vector: Seq[String]) extends Vector {
  vector = vector.distinct

  def toFrequencyVector: FrequencyVector = {
    val map = mutable.Map[String, Int]()
    vector foreach {
      term =>
        map(term) = 1
    }
    new FrequencyVector(map)
  }

  def sum: Long = {
    vector.size
  }

  def innerProduct(v2: BinaryVector): Long = {
    vector.intersect(v2.vector).size
  }
}
