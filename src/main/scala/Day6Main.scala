import java.text.{CharacterIterator, StringCharacterIterator}
import java.util.StringTokenizer
import java.util.function.Consumer
import java.util.regex.Pattern

import scala.util.matching.Regex

/**
  * @author ynupc
  *         Created on 2016/05/25
  */
object Day6Main {
  private val unagiCopula: String = "僕はウナギ"
  private val tautology: String = "ウナギはウナギだ。"
  private val gardenPathSentence: String = "次郎は花子に渡した\nウナギを呼びつけた。"
  private val gardenPathSentence2: String =
    """ 次郎は花子に渡した
      |ウナギを呼びつけた。
      |
    """.stripMargin.concat("\r")

  def main(args: Array[String]): Unit = {
    val executionTime: ExecutionTime = new ExecutionTime(1000000)

    println("== vs equals vs compare vs compareTo vs eq")
    executionTime.printlnAverageExecutionTime(unagiCopula == "僕はウナギ")
    executionTime.printlnAverageExecutionTime(unagiCopula.equals("僕はウナギ"))
    executionTime.printlnAverageExecutionTime(unagiCopula.compare("僕はウナギ") == 0)
    executionTime.printlnAverageExecutionTime(unagiCopula.compareTo("僕はウナギ") == 0)
    executionTime.printlnAverageExecutionTime(unagiCopula eq "僕はウナギ")
    println("!= vs !equals vs !compare vs !compareTo vs ne")
    executionTime.printlnAverageExecutionTime(unagiCopula != "僕はウサギ")
    executionTime.printlnAverageExecutionTime(!unagiCopula.equals("僕はウサギ"))
    executionTime.printlnAverageExecutionTime(unagiCopula.compare("僕はウサギ") != 0)
    executionTime.printlnAverageExecutionTime(unagiCopula.compareTo("僕はウサギ") != 0)
    executionTime.printlnAverageExecutionTime(unagiCopula ne "僕はウサギ")
    println("equalsIgnoreCase vs compareToIgnoreCase")
    executionTime.printlnAverageExecutionTime("I am Unagi.".equalsIgnoreCase("i am unagi."))
    executionTime.printlnAverageExecutionTime("I am Unagi.".compareToIgnoreCase("i am unagi.") == 0)
    println("!equalsIgnoreCase vs !compareToIgnoreCase")
    executionTime.printlnAverageExecutionTime(!"I am Unagi.".equalsIgnoreCase("i am usagi."))
    executionTime.printlnAverageExecutionTime("I am Unagi.".compareToIgnoreCase("i am usagi.") != 0)
    println("matches")
    executionTime.printlnAverageExecutionTime(unagiCopula.matches("僕はウナギ"))
    executionTime.printlnAverageExecutionTime(Pattern.matches("僕はウナギ", unagiCopula))
    executionTime.printlnAverageExecutionTime(Pattern.compile("僕はウナギ").matcher(unagiCopula).matches)
    val patternUnagiCopula: Pattern = Pattern.compile("僕はウナギ")
    executionTime.printlnAverageExecutionTime(patternUnagiCopula.matcher(unagiCopula).matches)
    executionTime.printlnAverageExecutionTime {
      val regex: Regex = """僕はウナギ""".r
      unagiCopula match {
        case regex() =>
          true
        case otherwise =>
          false
      }
    }
    val regexUnagiCopula: Regex = """僕はウナギ""".r
    executionTime.printlnAverageExecutionTime {
      unagiCopula match {
        case regexUnagiCopula() =>
          true
        case otherwise =>
          false
      }
    }
    println("contains vs containsSlice")
    executionTime.printlnAverageExecutionTime(unagiCopula.contains("ウナギ"))
    executionTime.printlnAverageExecutionTime(unagiCopula.containsSlice("ウナギ"))
    println("broad match")
    executionTime.printlnAverageExecutionTime(Pattern.compile("ウナギ").matcher(tautology).find)
    val patternUnagi: Pattern = Pattern.compile("ウナギ")
    executionTime.printlnAverageExecutionTime(patternUnagi.matcher(tautology).find)
    executionTime.printlnAverageExecutionTime("""ウナギ""".r.findFirstIn(tautology).nonEmpty)
    val regexUnagi: Regex = """ウナギ""".r
    executionTime.printlnAverageExecutionTime(regexUnagi.findFirstIn(tautology).nonEmpty)
    executionTime.printlnAverageExecutionTime("""ウナギ""".r.findFirstMatchIn(tautology).nonEmpty)
    executionTime.printlnAverageExecutionTime(regexUnagi.findFirstMatchIn(tautology).nonEmpty)
    println("broad match all")
    executionTime.printlnAverageExecutionTime("""ウナギ""".r.findAllIn(tautology).size)
    executionTime.printlnAverageExecutionTime(regexUnagi.findAllIn(tautology).size)
    executionTime.printlnAverageExecutionTime("""ウナギ""".r.findAllMatchIn(tautology).size)
    executionTime.printlnAverageExecutionTime(regexUnagi.findAllMatchIn(tautology).size)
    println("split")
    val csv: String = "A,B,C,D,E,F"
    val delimiter: String = ","
    executionTime.printlnAverageExecutionTime(csv.split(delimiter))
    executionTime.printlnAverageExecutionTime(Pattern.compile(delimiter).split(csv))
    val patternDelimiter: Pattern = Pattern.compile(delimiter)
    executionTime.printlnAverageExecutionTime(patternDelimiter.split(csv))
    executionTime.printlnAverageExecutionTime {
      Pattern.compile(delimiter).splitAsStream(csv) forEach {
        new Consumer[String]() {
          override def accept(str: String): Unit = {

          }
        }
      }
    }
    executionTime.printlnAverageExecutionTime {
      patternDelimiter.splitAsStream(csv) forEach {
        new Consumer[String]() {
          override def accept(str: String): Unit = {

          }
        }
      }
    }
    executionTime.printlnAverageExecutionTime {
      val stringTokenizer = new StringTokenizer(csv, delimiter)
      while (stringTokenizer.hasMoreTokens) {
        val token: String = stringTokenizer.nextToken
      }
    }
    println("split with limit")
    executionTime.printlnAverageExecutionTime(csv.split(delimiter, 3))
    executionTime.printlnAverageExecutionTime(Pattern.compile(delimiter).split(csv, 3))
    executionTime.printlnAverageExecutionTime(patternDelimiter.split(csv, 3))
    println("replace all")
    executionTime.printlnAverageExecutionTime(tautology.replace("ウナギ", "かめ"))
    executionTime.printlnAverageExecutionTime(tautology.replaceAllLiterally("ウナギ", "かめ"))
    println("replace first regex")
    executionTime.printlnAverageExecutionTime(tautology.replaceFirst("[ナニヌネノ]", "サ"))
    executionTime.printlnAverageExecutionTime(Pattern.compile("[ナニヌネノ]").matcher(tautology).replaceFirst("サ"))
    val patternNaNiNuNeNo: Pattern = Pattern.compile("[ナニヌネノ]")
    executionTime.printlnAverageExecutionTime(patternNaNiNuNeNo.matcher(tautology).replaceFirst("サ"))
    println("replace all regex")
    executionTime.printlnAverageExecutionTime(tautology.replaceAll("[ナニヌネノ]", "サ"))
    executionTime.printlnAverageExecutionTime(Pattern.compile("[ナニヌネノ]").matcher(tautology).replaceAll("サ"))
    executionTime.printlnAverageExecutionTime(patternNaNiNuNeNo.matcher(tautology).replaceAll("サ"))
    println("updated")
    executionTime.printlnAverageExecutionTime(tautology.updated(6, 'ジ'))
    executionTime.printlnAverageExecutionTime(tautology.patch(6, "ジ", 1))
    println("A < B")
    executionTime.printlnAverageExecutionTime('A' < 'B')
    executionTime.printlnAverageExecutionTime('A'.compare('B') < 0)
    executionTime.printlnAverageExecutionTime('A'.compareTo('B') < 0)
    println("B < BA")
    executionTime.printlnAverageExecutionTime("B" < "BA")
    executionTime.printlnAverageExecutionTime("B".compare("BA") < 0)
    executionTime.printlnAverageExecutionTime("B".compareTo("BA") < 0)
    println("charAt")
    executionTime.printlnAverageExecutionTime(tautology(3) == 'は')
    executionTime.printlnAverageExecutionTime(tautology.apply(3) == 'は')
    executionTime.printlnAverageExecutionTime(tautology.charAt(3) == 'は')
    println("take")
    executionTime.printlnAverageExecutionTime(tautology.take(3))
    executionTime.printlnAverageExecutionTime(tautology.substring(0, 3))
    executionTime.printlnAverageExecutionTime(tautology.dropRight(tautology.length - 3))
    println("takeRight")
    executionTime.printlnAverageExecutionTime(tautology.takeRight(3))
    executionTime.printlnAverageExecutionTime(tautology.substring(tautology.length - 3))
    executionTime.printlnAverageExecutionTime(tautology.drop(tautology.length - 3))
    println("filter")
    executionTime.printlnAverageExecutionTime {
      tautology filter {
        char =>
          'ア' <= char
      }
    }
    executionTime.printlnAverageExecutionTime {
      tautology withFilter {
        char =>
          'ア' <= char
      } map (char => char)
    }
    println("drop")
    executionTime.printlnAverageExecutionTime(tautology.drop(3))
    executionTime.printlnAverageExecutionTime(tautology.substring(3))
    executionTime.printlnAverageExecutionTime(tautology.takeRight(tautology.length - 3))
    println("dropRight")
    executionTime.printlnAverageExecutionTime(tautology.dropRight(3))
    executionTime.printlnAverageExecutionTime(tautology.substring(0, tautology.length - 3))
    executionTime.printlnAverageExecutionTime(tautology.take(tautology.length - 3))
    println("string list sort")
    val list: Seq[String] = Seq[String](
      "A", "B", "C", "D", "E", "F",
      "A", "AB", "AA", "BA",
      "a", "b", "c", "d", "e", "f",
      "あ", "い", "う", "え", "お",
      "ア", "イ", "ウ", "エ", "オ",
      "安", "以", "宇", "衣", "於",
      "。", "、", "．", "，", "𠮷"
    )
    executionTime.printlnAverageExecutionTime(list.sorted)
    executionTime.printlnAverageExecutionTime(list.sortWith((a, b) => a < b))
    executionTime.printlnAverageExecutionTime(list.sortWith(_ < _))
    executionTime.printlnAverageExecutionTime(list.sortWith((a, b) => a <= b))
    executionTime.printlnAverageExecutionTime(list.sortWith(_ <= _))
    println("string list reverse sort")
    executionTime.printlnAverageExecutionTime(list.sorted.reverse)
    executionTime.printlnAverageExecutionTime(list.sortWith((a, b) => a > b))
    executionTime.printlnAverageExecutionTime(list.sortWith(_ > _))
    executionTime.printlnAverageExecutionTime(list.sortWith((a, b) => a >= b))
    executionTime.printlnAverageExecutionTime(list.sortWith(_ >= _))
    println("string reverse sort")
    executionTime.printlnAverageExecutionTime("安衣宇以於\uD842\uDFB7".reverse)
    executionTime.printlnAverageExecutionTime {
      val builder: StringBuilder = new StringBuilder("安衣宇以於\uD842\uDFB7")
      builder.reverse.result
    }
    println("char max")
    executionTime.printlnAverageExecutionTime("安衣宇以於\uD842\uDFB7".max)
    executionTime.printlnAverageExecutionTime("安衣宇以於\uD842\uDFB7".maxBy(char => char))
    println("list max")
    executionTime.printlnAverageExecutionTime(Seq[String]("安", "衣", "宇", "以", "於", "𠮷").max)
    executionTime.printlnAverageExecutionTime(Seq[String]("安", "衣", "宇", "以", "於", "𠮷").maxBy(str => str))
    println("char min")
    executionTime.printlnAverageExecutionTime("安衣宇以於\uD842\uDFB7".min)
    executionTime.printlnAverageExecutionTime("安衣宇以於\uD842\uDFB7".minBy(char => char))
    println("list min")
    executionTime.printlnAverageExecutionTime(Seq[String]("安", "衣", "宇", "以", "於", "𠮷").min)
    executionTime.printlnAverageExecutionTime(Seq[String]("安", "衣", "宇", "以", "於", "𠮷").minBy(str => str))
    println("char diff")
    executionTime.printlnAverageExecutionTime(tautology.diff("ウ").diff("ウ"))
    executionTime.printlnAverageExecutionTime(tautology.diff("ウウ"))
    println("list diff")
    executionTime.printlnAverageExecutionTime(Seq[String]("安", "衣", "安", "安", "宇", "以", "於", "𠮷").diff(Seq[String]("安")).diff(Seq[String]("安")))
    executionTime.printlnAverageExecutionTime(Seq[String]("安", "衣", "安", "安", "宇", "以", "於", "𠮷").diff(Seq[String]("安安")))
    println("indexOf")
    executionTime.printlnAverageExecutionTime(tautology.indexOf("ナギ"))
    executionTime.printlnAverageExecutionTime(tautology.indexOfSlice("ナギ"))
    println("indexOf from")
    executionTime.printlnAverageExecutionTime(tautology.indexOf("ナギ", 3))
    executionTime.printlnAverageExecutionTime(tautology.indexOfSlice("ナギ", 3))
    println("lastIndexOf")
    executionTime.printlnAverageExecutionTime(tautology.lastIndexOf("ナギ"))
    executionTime.printlnAverageExecutionTime(tautology.lastIndexOfSlice("ナギ"))
    println("lastIndexOf from")
    executionTime.printlnAverageExecutionTime(tautology.lastIndexOf("ナギ", 3))
    executionTime.printlnAverageExecutionTime(tautology.lastIndexOfSlice("ナギ", 3))
    println("indexWhere")
    executionTime.printlnAverageExecutionTime {
      tautology.prefixLength(char => char != 'ギ')
    }
    executionTime.printlnAverageExecutionTime {
      tautology.segmentLength(char => char != 'ギ', 0)
    }
    executionTime.printlnAverageExecutionTime {
      tautology.indexWhere(char => char == 'ギ')
    }
    executionTime.printlnAverageExecutionTime {
      tautology.indexWhere(char => char == 'ギ', 0)
    }
    println("lastIndexWhere")
    executionTime.printlnAverageExecutionTime(tautology.lastIndexWhere(char => char == 'ギ'))
    executionTime.printlnAverageExecutionTime(tautology.lastIndexWhere(char => char == 'ギ', 0))
    println("iteration")
    executionTime.printlnAverageExecutionTime {
      unagiCopula foreach {
        char =>
      }
    }
    executionTime.printlnAverageExecutionTime {
      val iterator: CharacterIterator = new StringCharacterIterator(unagiCopula)
      var char: Char = iterator.first
      while (char != CharacterIterator.DONE) {
        char = iterator.next
      }
    }
    executionTime.printlnAverageExecutionTime {
      val iterator: Iterator[Char] = unagiCopula.iterator
      while (iterator.hasNext) {
        val char: Char = iterator.next
      }
    }

  }
}
