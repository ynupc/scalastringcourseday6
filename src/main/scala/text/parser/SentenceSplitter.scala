package text.parser

import util.Config

/**
  * @author ynupc
  *         Created on 2016/05/23
  */
object SentenceSplitter {
  def split(text: String): Seq[String] = {
    Config.sentenceSplitter match {
      case "none" =>
        Seq[String](text)
      case _ =>
        Seq[String](text)
    }
  }
}
