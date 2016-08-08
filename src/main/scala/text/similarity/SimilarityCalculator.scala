package text.similarity

import text.vector.FrequencyVector
import util.Config

/**
  * @author ynupc
  *         Created on 2016/05/23
  */
object SimilarityCalculator {
  def calculate(v1: FrequencyVector, v2: FrequencyVector): Double = {
    Similarity.calculate(v1, v2, Config.similarity)
  }
}

class SimilarityCalculator(val vector: FrequencyVector) {
  def calculate(v2: FrequencyVector): Double = {
    Similarity.calculate(vector, v2, Config.similarity)
  }
}

class AverageSimilarityCalculator(val vectors: Seq[FrequencyVector]) {
  val calculators: Seq[SimilarityCalculator] = {
    for (vector <- vectors) yield {
      new SimilarityCalculator(vector)
    }
  }
  val size = calculators.size
  def calculate(v2: FrequencyVector): Double = {
    var score: Double = 0D
    calculators foreach {
      calculator: SimilarityCalculator =>
        score += calculator.calculate(v2)
    }
    Divider.divide(score, size)
  }
}