package text.vector

/**
  * @author ynupc
  *         Created on 2016/05/22
  */
trait VectorMerger[Vector] {
  def merge(vectors: Seq[Vector]): Vector
}