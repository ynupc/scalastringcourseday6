package util

/**
  * @author ynupc
  *         Created on 2016/08/07
  */
object StringUtils {
  implicit def stringToStringUtils(repr: CharSequence): StringUtils = new StringUtils(repr)
}

class StringUtils(repr: CharSequence) {
  private def charSequence2String: String = {
    repr match {
      case str: String =>
        str
      case otherwise =>
        otherwise.toString
    }
  }

  def replaceAllLiteratim(target: CharSequence, replacement: CharSequence): String = {
    charSequence2String.replace(target, replacement)
  }

  def quote(quotation: (String, String)): String = {
    new StringBuilder().
      append(quotation._1).
      append(repr).
      append(quotation._2).
      result
  }

  def codePointCount: Int = {
    toCodePointArray.length
  }

  def toCodePointArray: Array[Int] = {
    if (repr == null) {
      throw new NullPointerException
    }

    val charArray: Array[Char] = charSequence2String.toCharArray
    val length: Int = charArray.length
    var surrogatePairCount: Int = 0
    var isSkipped: Boolean = false
    for (i <- 0 until length) {
      if (isSkipped) {
        isSkipped = false
      } else {
        if (0 < i && Character.isSurrogatePair(charArray(i - 1), charArray(i))) {
          surrogatePairCount += 1
          isSkipped = true
        }
      }
    }
    isSkipped = false
    val codePoints: Array[Int] = new Array[Int](length - surrogatePairCount)
    var j: Int = 0
    for (i <- 0 until length) {
      if (isSkipped) {
        isSkipped = false
      } else {
        val currentChar = charArray(i)
        if (Character.isHighSurrogate(currentChar) && i + 1 < length) {
          val nextChar = charArray(i + 1)
          if (Character.isLowSurrogate(nextChar)) {
            codePoints(j) = Character.toCodePoint(currentChar, nextChar)
            j += 1
            isSkipped = true
          }
        }
        if (!isSkipped) {
          codePoints(j) = currentChar
          j += 1
        }
      }
    }
    codePoints
  }
}
