package text.vector

import scala.collection.mutable.ListBuffer

/**
  * @author ynupc
  *         Created on 2016/05/22
  */
object FrequencyVectorMerger extends VectorMerger[FrequencyVector] {
  override def merge(vectors: Seq[FrequencyVector]): FrequencyVector = {
    val terms = ListBuffer[(String, Int)]()
    vectors foreach {
      vector =>
        val v = vector.vector
        terms ++= v.toList
    }
    FrequencyVectorGenerator.getVector(terms.result())
  }
}