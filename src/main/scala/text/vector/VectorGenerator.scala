package text.vector

import scala.collection.mutable

/**
  * @author ynupc
  *         Created on 2016/05/22
  */
trait VectorGenerator[Vector] {
  private val cache = mutable.Map[Long, Vector]()

  def getVectorFromCache(id: Long, sentence: String): Vector = {
    if (cache.contains(id)) {
      cache(id)
    } else {
      val vector = getVectorFromSentence(sentence)
      cache(id) = vector
      vector
    }
  }

  def getVectorFromText(text: String): Vector

  def getVectorFromSentence(sentence: String): Vector
}
