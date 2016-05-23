package text.vector

/**
  * @author ynupc
  *         Created on 2016/05/22
  */
class FrequencyVector(val vector: scala.collection.mutable.Map[String, Int]) extends Vector {
  def apply(key: String): Int = {
    if (vector contains key) {
      vector(key)
    } else {
      0
    }
  }

  def increment(key: String): Unit = {
    if (vector contains key) {
      vector(key) += 1
    } else {
      vector(key) = 1
    }
  }

  def toBinaryVector: BinaryVector = {
    new BinaryVector(vector.keys.toSeq)
  }

  def sum: Long = {
    var summation = 0L
    vector foreach {
      case (key, value) =>
        summation += value
    }
    summation
  }

  def squareSum: Long = {
    var summation = 0L
    vector foreach {
      case (key, value) =>
        summation += value * value
    }
    summation
  }

  def keySum: Long = {
    vector.keySet.size
  }

  def keySum(v2: FrequencyVector): Long = {
    val keySet = vector.keySet
    var summation: Long = keySet.size
    v2.vector.keys foreach {
      case key if keySet contains key =>
      case otherwise =>
        summation += 1
    }
    summation
  }

  def average: Double = {
    sum / keySum
  }

  def variance: Double = {
    val avg = average
    var summation = 0D
    vector foreach {
      case (key, value) =>
        val diff = value - avg
        summation += diff * diff
    }
    summation / vector.size
  }

  def standardDeviation: Double = {
    math.sqrt(variance)
  }

  def covariance(v2: FrequencyVector): Double = {
    val keySet = vector.keySet | v2.vector.keySet
    val size = keySet.size

    val avgV1 = sum / size
    val avgV2 = v2.sum / size

    var summation = 0D
    keySet foreach {
      key =>
        summation += (this(key) - avgV1) * (v2(key) - avgV2)
    }
    summation / size
  }

  def innerProduct(v2: FrequencyVector): Long = {
    val vector2 = v2.vector
    val keySetV2 = vector2.keySet
    var summation = 0L
    vector foreach {
      case (key1, value1) if keySetV2 contains key1 =>
        summation += value1 * vector2(key1)
      case otherwise =>
    }
    summation
  }
}
