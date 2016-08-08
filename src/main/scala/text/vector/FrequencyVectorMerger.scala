package text.vector

import scala.collection.mutable.ListBuffer

/**
  * @author ynupc
  *         Created on 2016/05/22
  */
object FrequencyVectorMerger extends VectorMerger[FrequencyVector] {
  override def merge(vectors: Seq[FrequencyVector]): FrequencyVector = {
    val terms: ListBuffer[(String, Int)] = ListBuffer[(String, Int)]()
    vectors foreach {
      vector: FrequencyVector =>
        terms ++= vector.vector.toList
    }
    FrequencyVectorGenerator.getVector(terms.result())
  }
}