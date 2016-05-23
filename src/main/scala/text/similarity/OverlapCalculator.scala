package text.similarity

import text.vector.BinaryVector
import util.Config

/**
  * @author ynupc
  *         Created on 2016/05/23
  */
object OverlapCalculator {
  def calculate(v1: BinaryVector, v2: BinaryVector): Double = {
    Overlap.calculate(v1, v2, Config.convergence)
  }
}

class OverlapCalculator(val vector: BinaryVector) {
  def calculate(v2: BinaryVector): Double = {
    Overlap.calculate(vector, v2, Config.convergence)
  }
}

class AverageOverlapCalculator(val vectors: Seq[BinaryVector]) {
  val calculators = {
    for (vector <- vectors) yield {
      new OverlapCalculator(vector)
    }
  }
  val size = calculators.size
  def calculate(v2: BinaryVector): Double = {
    var score = 0D
    var counter = 0
    calculators foreach {
      calculator =>
        counter += 1
        score += calculator.calculate(v2)
    }
    Divider.divide(score, size)
  }
}
