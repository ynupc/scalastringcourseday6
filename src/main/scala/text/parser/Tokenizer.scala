package text.parser

import util.Config
import util.StringUtils._

/**
  * @author ynupc
  *         Created on 2016/05/23
  */
object Tokenizer {
  type Tokens = Seq[String]

  def tokenize(textOpt: Option[String]): Tokens = {
    textOpt match {
      case Some(text) =>
        Config.tokenizer match {
          case that if that equalsIgnoreCase "CharacterNGram" =>
            characterNGram(text)
          case otherwise =>
            Nil
        }
      case otherwise =>
        Nil
    }
  }

  private def characterNGram(text: String): Tokens = {
    val codePoints: Seq[Int] = text.toCodePointArray.toSeq
    codePoints.sliding(Config.nGram).toSeq map {
      codePoint =>
        val array: Array[Int] = codePoint.toArray
        new String(array, 0, array.length)
    }
  }
}
