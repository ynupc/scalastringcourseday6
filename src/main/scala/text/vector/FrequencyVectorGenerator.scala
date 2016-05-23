package text.vector

import text.parser.{SentenceSplitter, Tokenizer}

import scala.collection.mutable

/**
  * @author ynupc
  *         Created on 2016/05/22
  */
object FrequencyVectorGenerator extends VectorGenerator[FrequencyVector] {
  override def getVectorFromText(text: String): FrequencyVector = {
    getVector(
      //Seq[Seq[(String, Int)]]からList[Set[(String, Int)]]を経由してSeq[(String, Int)]に変換した。
      {
        for (sentence <- SentenceSplitter.parse(text)) yield {
          getVectorFromSentence(sentence).vector.toSet
        }
      }.toList.flatten
    )
  }

  override def getVectorFromSentence(sentence: String): FrequencyVector = {
    getVector(
      Tokenizer.tokenize(Option(sentence)).zipAll(Seq[Int](), "", 1)
    )
  }

  def getVector(terms: Seq[(String, Int)]): FrequencyVector = {
    val vector = mutable.Map[String, Int]()

    def add(term: String, frequency: Int): Unit = {
      if (vector contains term) {
        vector(term) += frequency
      } else {
        vector(term) = frequency
      }
    }

    terms foreach {
      case (term, frequency) =>
        add(term, frequency)
    }

    val frequencyVector = new FrequencyVector(vector)

    VectorType.get match {
      case VectorType.Binary =>
        frequencyVector.
          toBinaryVector.
          toFrequencyVector
      case VectorType.Frequency |
           VectorType.None =>
        frequencyVector
    }
  }
}
