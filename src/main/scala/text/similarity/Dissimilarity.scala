package text.similarity

import text.vector.FrequencyVector
import util.Config

/**
  * @author ynupc
  *         Created on 2016/05/23
  */
object Dissimilarity extends Enumeration {
  val None,
  Chebyshev,//unnormalized
  Euclidean,//unnormalized
  Manhattan,//unnormalized
  Minkowsky//unnormalized
  = Value

  def calculate(v1: FrequencyVector, v2: FrequencyVector, dissimilarity: Dissimilarity.Value): Double = {
    val defaultDissimilarity: Double = 0D
    dissimilarity match {
      case Chebyshev =>
        calculateChebyshev(v1, v2)
      case Euclidean =>
        calculateEuclidean(v1, v2)
      case Manhattan =>
        calculateManhattan(v1, v2)
      case Minkowsky =>
        calculateMinkowsky(v1, v2, Config.minkowskyQ)
      case _ =>
        defaultDissimilarity
    }
  }

  def calculateChebyshev(v1: FrequencyVector, v2: FrequencyVector): Double = {
    var distance = 0
    val vector1 = v1.vector
    val vector2 = v2.vector
    vector1 foreach {
      case (key1, value1) =>
        if (vector2.contains(key1)) {
          val diff = math.abs(value1 - vector2(key1))
          if (distance < diff) {
            distance = diff
          }
        } else if (distance < value1) {
          distance = value1
        }
    }
    vector2.keySet.diff(vector1.keySet) foreach {
      key2 =>
        val value2 = vector2(key2)
        if (distance < value2) {
          distance = value2
        }
    }
    distance
  }

  def calculateEuclidean(v1: FrequencyVector, v2: FrequencyVector): Double = {
    calculateMinkowsky(v1, v2, 2D)
  }

  def calculateManhattan(v1: FrequencyVector, v2: FrequencyVector): Double = {
    calculateMinkowsky(v1, v2, 1D)
  }

  def calculateMinkowsky(v1: FrequencyVector, v2: FrequencyVector, q: Double): Double = {
    val vector1 = v1.vector
    val vector2 = v2.vector
    var summation = 0D
    vector1 foreach {
      case (key1, value1) =>
        if (vector2.contains(key1)) {
          val diff = math.abs(value1 - vector2(key1))
          summation += math.pow(diff, q)
        } else {
          summation += math.pow(value1, q)
        }
    }
    vector2.keySet.diff(vector1.keySet).foreach {
      key2 =>
        val value2 = vector2(key2)
        summation += math.pow(value2, q)
    }
    math.pow(summation, 1 / q)
  }
}