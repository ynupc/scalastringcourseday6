package text.vector

import text.parser.{SentenceSplitter, Tokenizer}

/**
  * @author ynupc
  *         Created on 2016/05/22
  */
object BinaryVectorGenerator extends VectorGenerator[BinaryVector] {
  override def getVectorFromText(text: String): BinaryVector = {
    BinaryVectorMerger.merge(
      for (sentence <- SentenceSplitter.split(text)) yield {
        getVectorFromSentence(sentence)
      }
    )
  }

  override def getVectorFromSentence(sentence: String): BinaryVector = {
    new BinaryVector(Tokenizer.tokenize(Option(sentence)).distinct)
  }
}
