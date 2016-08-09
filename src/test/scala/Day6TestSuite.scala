import java.text.{CharacterIterator, StringCharacterIterator}
import java.util.StringTokenizer
import java.util.function.Consumer
import java.util.regex.{Matcher, Pattern}

import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import text.parser.Tokenizer
import text.similarity._
import text.vector.{BinaryVector, BinaryVectorGenerator, FrequencyVector, FrequencyVectorGenerator}

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex
import scala.util.matching.Regex.{Match, MatchIterator}

/**
  * @author ynupc
  *         Created on 2016/02/07
  */
class Day6TestSuite extends AssertionsForJUnit {
  //太郎「何にする？」
  private val unagiCopula: String = "僕はウナギ"
  //太郎「ウナギって何？」
  private val tautology: String = "ウナギはウナギだ。"
  private val gardenPathSentence: String = "次郎は花子に渡した\nウナギを呼びつけた。"
  //ウナギ「僕はウナギ」
  private val gardenPathSentence2: String =
    """ 次郎は花子に渡した
      |ウナギを呼びつけた。
      |
    """.stripMargin.concat("\r")

  @Test
  def testStringEqualExpression(): Unit = {
    assert(unagiCopula == "僕はウナギ")
    assert(unagiCopula.equals("僕はウナギ"))
    assert(unagiCopula.compare("僕はウナギ") == 0)
    assert(unagiCopula.compareTo("僕はウナギ") == 0)

    assert(unagiCopula != "僕はウサギ")
    assert(!unagiCopula.equals("僕はウサギ"))
    assert(unagiCopula.compare("僕はウサギ") != 0)
    assert(unagiCopula.compareTo("僕はウサギ") != 0)

    assert(unagiCopula != null)
    assert(!unagiCopula.equals(null))
    //java.lang.NullPointerException
    //assert(unagiCopula.compare(null) != 0)
    //java.lang.NullPointerException
    //assert(unagiCopula.compareTo(null) != 0)
  }

  @Test
  def testStringEqualExpressionIngnoreCase(): Unit = {
    assert(!"I am Unagi.".equals("i am unagi."))
    assert("I am Unagi.".equalsIgnoreCase("i am unagi."))
    assert("I am Unagi.".compareToIgnoreCase("i am unagi.") == 0)

    assert(!"I am Unagi.".equalsIgnoreCase(null))
    //java.lang.NullPointerException
    //assert("I am Unagi.".compareToIgnoreCase(null) != 0)
  }

  @Test
  def testStringEqualExpression2(): Unit = {
    assert(unagiCopula eq "僕はウナギ")
    assert(unagiCopula ne "僕はウサギ")
  }

  @Test
  def testExactMatch1(): Unit = {
    assert(unagiCopula.matches("僕はウナギ"))
  }

  @Test
  def testExactMatch2(): Unit = {
    assert(Pattern.matches("僕はウナギ", unagiCopula))
  }

  @Test
  def testExactMatch3(): Unit = {
    val pattern: Pattern = Pattern.compile("僕はウナギ")
    val matcher: Matcher = pattern.matcher(unagiCopula)

    assert(matcher.matches)
  }

  @Test
  def testExactMatch4(): Unit = {
    val regex: Regex = """僕はウナギ""".r
    unagiCopula match {
      case regex() =>
        assert(true)
      case otherwise =>
        assert(false)
    }
  }

  @Test
  def testContains(): Unit = {
    assert(unagiCopula.contains("ウナギ"))
    assert(unagiCopula.containsSlice("ウナギ"))
  }

  @Test
  def testBroadMatch1(): Unit = {
    val pattern: Pattern = Pattern.compile("ウナギ")
    val matcher: Matcher = pattern.matcher(tautology)

    assert(matcher.find)
  }

  @Test
  def testBroadMatch2(): Unit = {
    val regex: Regex = """ウナギ""".r
    val firstIn: Option[String] = regex.findFirstIn(tautology)

    assert(firstIn.nonEmpty)
  }

  @Test
  def testBroadMatch3(): Unit = {
    val regex: Regex = """ウナギ""".r
    val allIn: MatchIterator = regex.findAllIn(tautology)

    assert(allIn.nonEmpty)
    assert(allIn.size == 2)
  }

  @Test
  def testBroadMatch4(): Unit = {
    val regex: Regex = """ウナギ""".r
    val firstMatch: Option[Match] = regex.findFirstMatchIn(tautology)

    assert(firstMatch.nonEmpty)
  }

  @Test
  def testBroadMatch5(): Unit = {
    val regex: Regex = """ウナギ""".r
    val allMatch: Iterator[Match] = regex.findAllMatchIn(tautology)

    assert(allMatch.nonEmpty)
    assert(allMatch.size == 2)
  }

  @Test
  def testStartsWith(): Unit = {
    assert(tautology.startsWith("ウナギ"))
  }

  @Test
  def testForwardMatch(): Unit = {
    val pattern: Pattern = Pattern.compile("ウナギ")
    val matcher: Matcher = pattern.matcher(tautology)

    assert(matcher.lookingAt)
  }

  @Test
  def testEndsWith(): Unit = {
    assert(tautology.endsWith("ギだ。"))
  }

  @Test
  def testBackwardMatch(): Unit = {
    val pattern: Pattern = Pattern.compile("。だギ")
    val matcher: Matcher = pattern.matcher(tautology.reverse)

    assert(matcher.lookingAt)
  }

  @Test
  def testLongestMatchingAndShortestMatching(): Unit = {
    val str: String = "<a>いろは</a>"
    val longestMatchingRegex1: Regex = "<.+>".r
    val longestMatchingRegex2: Regex = "<.*>".r
    val shortestMatchingRegex1: Regex = "<[^>]+>".r
    val shortestMatchingRegex2: Regex = "<[^>]*>".r
    //最長一致
    assert(longestMatchingRegex1.findFirstIn(str).get == "<a>いろは</a>")
    assert(longestMatchingRegex2.findFirstIn(str).get == "<a>いろは</a>")
    //最短一致
    assert(shortestMatchingRegex1.findFirstIn(str).get == "<a>")
    assert(shortestMatchingRegex2.findFirstIn(str).get == "<a>")
  }

  @Test
  def testSplit1(): Unit = {
    val csv: String = "A,B,C,D,E,F"
    val delimiter: String = ","
    val tokens: Array[String] = csv.split(delimiter)
    assert(tokens.sameElements(Array[String]("A", "B", "C", "D", "E", "F")))
  }

  @Test
  def testSplit2(): Unit = {
    val csv: String = "A,B,C,D,E,F"
    val delimiter: String = ","
    val limit: Int = 3
    val tokens: Array[String] = csv.split(delimiter, limit)
    assert(tokens.sameElements(Array[String]("A", "B", "C,D,E,F")))

    val csv2: String = "A,B"
    val tokens2: Array[String] = csv2.split(delimiter, limit)
    assert(tokens2.sameElements(Array[String]("A", "B")))
  }

  @Test
  def testSplit3(): Unit = {
    val csv: String = "A,B,C,D,E,F"
    val delimiter: Pattern = Pattern.compile(",")
    val tokens: Array[String] = delimiter.split(csv)
    assert(tokens.sameElements(Array[String]("A", "B", "C", "D", "E", "F")))
  }

  @Test
  def testSplit4(): Unit = {
    val csv: String = "A,B,C,D,E,F"
    val delimiter: Pattern = Pattern.compile(",")
    val limit: Int = 3
    val tokens: Array[String] = delimiter.split(csv, limit)
    assert(tokens.sameElements(Array[String]("A", "B", "C,D,E,F")))

    val csv2: String = "A,B"
    val tokens2: Array[String] = delimiter.split(csv2, limit)
    assert(tokens2.sameElements(Array[String]("A", "B")))
  }

  @Test
  def testSplit5(): Unit = {
    val csv: String = "A,B,C,D,E,F"
    val delimiter: Pattern = Pattern.compile(",")
    val tokens: java.util.stream.Stream[String] = delimiter.splitAsStream(csv)
    val results: Array[String] = Array[String]("A", "B", "C", "D", "E", "F")
    var i = 0
    tokens.forEach(
      new Consumer[String]() {
        override def accept(str: String): Unit = {
          assert(str == results(i))
          i += 1
        }
      }
    )
  }

  @Test
  def testStringTokenizer(): Unit = {
    //StringTokenizerは互換性を保つためにJavaが残しているlegacy classですので、可能な限り使用は避けましょう。
    val stringTokenizer = new StringTokenizer("A,B,C,D,E,F", ",")
    val buffer: ListBuffer[String] = ListBuffer[String]()

    while (stringTokenizer.hasMoreTokens) {
      buffer += stringTokenizer.nextToken
    }

    assert(buffer == Seq[String]("A", "B", "C", "D", "E", "F"))
  }

  @Test
  def testSplitAt(): Unit = {
    val csv: String = "A,B,C,D,E,F"
    val index: Int = 3
    //indexの位置で分割する
    val pair: (String, String) = csv.splitAt(index)
    assert(pair._1 == "A,B")
    assert(pair._2 == ",C,D,E,F")

    val swappedPair: (String, String) = pair.swap
    assert(swappedPair._1 == ",C,D,E,F")
    assert(swappedPair._2 == "A,B")
  }

  @Test
  def testSpan(): Unit = {
    val csv: String = "A,B,C,D,E,F"
    val pair: (String, String) = csv span {
      char =>
        //条件に従わなくなったところで分割する
        char != 'C'
    }

    assert(pair._1 == "A,B,")
    assert(pair._2 == "C,D,E,F")
  }

  @Test
  def testSeparateLines1(): Unit = {
    val multiLine: String = "A,B\nC,D,E\fF"
    //行末文字：
    //LF(line feed, 改行, \n, 0x0A)
    //FF(form feed, 改ページ, \f, 0x0C)
    //で分割し、
    //行末文字は削除する
    val it: Iterator[String] = multiLine.lines

    val buffer: ListBuffer[String] = ListBuffer[String]()
    while (it.hasNext) {
      buffer += it.next
    }

    assert(buffer == Seq[String]("A,B", "C,D,E", "F"))
  }

  @Test
  def testSeparateLines2(): Unit = {
    val multiLine: String = "A,B\nC,D,E\fF"
    //行末文字：
    //LF(line feed, 改行, \n, 0x0A)
    //FF(form feed, 改ページ, \f, 0x0C)
    //で分割し、
    //行末文字を残す
    val it: Iterator[String] = multiLine.linesWithSeparators

    val buffer: ListBuffer[String] = ListBuffer[String]()
    while (it.hasNext) {
      buffer += it.next
    }

    assert(buffer == Seq[String]("A,B\n", "C,D,E\f", "F"))
  }

  @Test
  def testExtractByPattern1(): Unit = {
    val pattern: Pattern = Pattern.compile("ウ((ナ)(ギ))")
    val matcher: Matcher = pattern.matcher(tautology)

    while (matcher.find) {
      val group: String = matcher.group

      assert(group == "ウナギ")
    }
  }

  @Test
  def testExtractByPattern2(): Unit = {
    val pattern: Pattern = Pattern.compile("ウ((ナ)(ギ))")
    val matcher: Matcher = pattern.matcher(tautology)

    while (matcher.find) {
      val group0: String = matcher.group(0)
      val group1: String = matcher.group(1)
      val group2: String = matcher.group(2)
      val group3: String = matcher.group(3)
      //java.lang.IndexOutOfBoundsException: No group 4
      //val group3: String = matcher.group(4)

      assert(group0 == "ウナギ")
      assert(group1 == "ナギ")
      assert(group2 == "ナ")
      assert(group3 == "ギ")
    }
  }

  @Test
  def testExtractByPattern3(): Unit = {
    val pattern: Pattern = Pattern.compile("ウ(?<first>(?<second>ナ)(?<third>ギ))")
    val matcher: Matcher = pattern.matcher(tautology)

    while (matcher.find) {
      val group0: String = matcher.group()
      val group1: String = matcher.group("first")
      val group2: String = matcher.group("second")
      val group3: String = matcher.group("third")

      assert(group0 == "ウナギ")
      assert(group1 == "ナギ")
      assert(group2 == "ナ")
      assert(group3 == "ギ")
    }
  }

  @Test
  def testExtractByPattern4(): Unit = {
    val pattern: Pattern = Pattern.compile("ウ(?<first>(?<second>ナ)(?<third>ギ))")
    val matcher: Matcher = pattern.matcher(tautology)

    var counter: Int = 0

    while (matcher.find) {
      counter += 1

      assert(matcher.groupCount() == 3)

      counter match {
        case 1 =>
          assert(matcher.start()           == 0)
          assert(matcher.end()             == 3)
          assert(tautology.substring(0, 3) == "ウナギ")
          assert(matcher.group()           == "ウナギ")
          assert(matcher.group(0)          == "ウナギ")

          assert(matcher.start("first")    == 1)
          assert(matcher.start(1)          == 1)
          assert(matcher.end("first")      == 3)
          assert(matcher.end(1)            == 3)
          assert(tautology.substring(1, 3) == "ナギ")
          assert(matcher.group("first")    == "ナギ")
          assert(matcher.group(1)          == "ナギ")

          assert(matcher.start("second")   == 1)
          assert(matcher.start(2)          == 1)
          assert(matcher.end("second")     == 2)
          assert(matcher.end(2)            == 2)
          assert(tautology.substring(1, 2) == "ナ")
          assert(matcher.group("second")   == "ナ")
          assert(matcher.group(2)          == "ナ")

          assert(matcher.start("third")    == 2)
          assert(matcher.start(3)          == 2)
          assert(matcher.end("third")      == 3)
          assert(matcher.end(3)            == 3)
          assert(tautology.substring(2, 3) == "ギ")
          assert(matcher.group("third")    == "ギ")
          assert(matcher.group(3)          == "ギ")

        case 2 =>
          assert(matcher.start()           == 4)
          assert(matcher.end()             == 7)
          assert(tautology.substring(4, 7) == "ウナギ")
          assert(matcher.group()           == "ウナギ")
          assert(matcher.group(0)          == "ウナギ")

          assert(matcher.start("first")    == 5)
          assert(matcher.start(1)          == 5)
          assert(matcher.end("first")      == 7)
          assert(matcher.end(1)            == 7)
          assert(tautology.substring(5, 7) == "ナギ")
          assert(matcher.group("first")    == "ナギ")
          assert(matcher.group(1)          == "ナギ")

          assert(matcher.start("second")   == 5)
          assert(matcher.start(2)          == 5)
          assert(matcher.end("second")     == 6)
          assert(matcher.end(2)            == 6)
          assert(tautology.substring(5, 6) == "ナ")
          assert(matcher.group("second")   == "ナ")
          assert(matcher.group(2)          == "ナ")

          assert(matcher.start("third")    == 6)
          assert(matcher.start(3)          == 6)
          assert(matcher.end("third")      == 7)
          assert(matcher.end(3)            == 7)
          assert(tautology.substring(6, 7) == "ギ")
          assert(matcher.group("third")    == "ギ")
          assert(matcher.group(3)          == "ギ")

        case otherwise =>
          assert(false)
      }
    }

    assert(counter == 2)
  }

  @Test
  def testExtractByRegex1(): Unit = {
    val regex: Regex = "ウ((ナ)(ギ))".r

    //完全一致
    val regex(first, second, third) = "ウナギ"

    //val regex(first, second, third) = tautology
    //scala.MatchError: ウナギはウナギだ。 (of class java.lang.String)

    assert(first  == "ナギ")
    assert(second == "ナ")
    assert(third  == "ギ")
  }

  @Test
  def testExtractByRegex2(): Unit = {
    val regex: Regex = "ウ((ナ)(ギ))".r

    "ウナギ" match {
      //完全一致
      case regex(first, second, third) =>
        assert(first  == "ナギ")
        assert(second == "ナ")
        assert(third  == "ギ")
      case otherwise =>
        assert(false)
    }
  }

  @Test
  def testExtractByRegex3(): Unit = {
    val regex: Regex = "ウ((ナ)(ギ))".r

    //部分一致（前方から解析して最初の一致）
    regex.findFirstIn(tautology) match {
      //完全一致
      case Some(regex(first, second, third)) =>
        assert(first  == "ナギ")
        assert(second == "ナ")
        assert(third  == "ギ")
      case None =>
        assert(false)
    }
  }

  @Test
  def testExtractByRegex4(): Unit = {
    val regex: Regex = "ウ((ナ)(ギ))".r

    //完全一致
    for (regex(first, second, third)
            //部分一致（前方から解析して最初の一致）
         <- regex.findFirstIn(tautology)) {
      assert(first  == "ナギ")
      assert(second == "ナ")
      assert(third  == "ギ")
    }
  }

  @Test
  def testExtractByRegex5(): Unit = {
    val regex: Regex = new scala.util.matching.Regex("ウ((ナ)(ギ))", "first", "second", "third")

    //部分一致（前方から解析して最初の一致）
    for (m <- regex.findFirstMatchIn(tautology)) {
      assert(tautology.substring(m.start, m.end) == "ウナギ")

      assert(m.groupCount == 3)
      assert(m.groupNames == Seq[String]("first", "second", "third"))

      assert(m.group("first")                          == "ナギ")
      assert(m.group(1)                                == "ナギ")
      assert(tautology.substring(m.start(1), m.end(1)) == "ナギ")

      assert(m.group("second")                         == "ナ")
      assert(m.group(2)                                == "ナ")
      assert(tautology.substring(m.start(2), m.end(2)) == "ナ")

      assert(m.group("third")                          == "ギ")
      assert(m.group(3)                                == "ギ")
      assert(tautology.substring(m.start(3), m.end(3)) == "ギ")
    }
  }

  @Test
  def testExtractByRegex6(): Unit = {
    val regex: Regex = new scala.util.matching.Regex("ウ(?<first>(?<second>ナ)(?<third>ギ))")

    //部分一致（前方から解析して最初の一致）
    for (m <- regex.findFirstMatchIn(tautology)) {
      assert(tautology.substring(m.start, m.end) == "ウナギ")

      assert(m.groupCount == 3)
      //assert(m.groupNames == Seq[String]("first", "second", "third"))
      //org.scalatest.junit.JUnitTestFailedError: Array() did not equal List("first", "second", "third")

      //assert(m.group("first")                          == "ナギ")
      //java.util.NoSuchElementException: group name first not defined
      assert(m.group(1)                                == "ナギ")
      assert(tautology.substring(m.start(1), m.end(1)) == "ナギ")

      //assert(m.group("second")                         == "ナ")
      //java.util.NoSuchElementException: group name second not defined
      assert(m.group(2)                                == "ナ")
      assert(tautology.substring(m.start(2), m.end(2)) == "ナ")

      //assert(m.group("third")                          == "ギ")
      //java.util.NoSuchElementException: group name third not defined
      assert(m.group(3)                                == "ギ")
      assert(tautology.substring(m.start(3), m.end(3)) == "ギ")
    }
  }

  @Test
  def testExtractByRegex7(): Unit = {
    val regex: Regex = "ウ((ナ)(ギ))".r

    //部分一致（前方から解析して一致する全て）
    val matches: Regex.MatchIterator = regex.findAllIn(tautology)

    assert(matches.size == 2)

    matches foreach {
      //完全一致
      case regex(first, second, third) =>
        assert(first  == "ナギ")
        assert(second == "ナ")
        assert(third  == "ギ")
      case otherwise =>
        assert(false)
    }
  }

  @Test
  def testExtractByRegex8(): Unit = {
    val regex: Regex = new scala.util.matching.Regex("ウ((ナ)(ギ))", "first", "second", "third")

    //部分一致（前方から解析して一致する全て）
    val matches: Iterator[Regex.Match] = regex.findAllMatchIn(tautology)

    assert(matches.size == 2)

    matches foreach {
      m =>
        assert(tautology.substring(m.start, m.end) == "ウナギ")

        assert(m.groupCount == 3)
        assert(m.groupNames == Seq[String]("first", "second", "third"))

        assert(m.group("first")                          == "ナギ")
        assert(m.group(1)                                == "ナギ")
        assert(tautology.substring(m.start(1), m.end(1)) == "ナギ")

        assert(m.group("second")                         == "ナ")
        assert(m.group(2)                                == "ナ")
        assert(tautology.substring(m.start(2), m.end(2)) == "ナ")

        assert(m.group("third")                          == "ギ")
        assert(m.group(3)                                == "ギ")
        assert(tautology.substring(m.start(3), m.end(3)) == "ギ")
    }
  }

  @Test
  def testReplace1(): Unit = {
    //置換前の状態の確認用
    assert(tautology == "ウナギはウナギだ。")

    //文字に一致した全ての箇所を置換
    val replace1: String = tautology.replace('ナ', 'サ')
    assert(replace1 == "ウサギはウサギだ。")

    //文字列に一致した全ての箇所を置換
    val replace2: String = tautology.replace("ウナギ", "かめ")
    assert(replace2 == "かめはかめだ。")

    //文字列に一致した全ての箇所を置換
    val replace3: String = tautology.replaceAllLiterally("ウナギ", "かめ")
    assert(replace3 == "かめはかめだ。")
  }

  @Test
  def testReplace2(): Unit = {
    //置換前の状態の確認用
    assert(tautology == "ウナギはウナギだ。")

    //正規表現に最初に一致した箇所のみ置換
    val replaceFirst: String = tautology.replaceFirst(
      "[ナニヌネノ]",//カタカナのナ行の１文字を表す正規表現
      "サ")
    //最初の「ナ」は「サ」に置換されています。
    assert(replaceFirst == "ウサギはウナギだ。")

    //正規表現に一致した全ての箇所を置換
    val replaceAll: String = tautology.replaceAll(
      "[ナニヌネノ]",//カタカナのナ行の１文字を表す正規表現
      "サ")
    //「ナ」が全て「サ」に置換されています。
    assert(replaceAll == "ウサギはウサギだ。")
  }

  @Test
  def testReplace3(): Unit = {
    //カタカナのナ行の１文字を表す正規表現
    val pattern: Pattern = Pattern.compile("[ナニヌネノ]")
    val matcher: Matcher = pattern.matcher(tautology)

    //正規表現に最初に一致した箇所のみ置換
    val replaceFirst: String = matcher.replaceFirst("サ")
    assert(replaceFirst == "ウサギはウナギだ。")

    //正規表現に一致した全ての箇所を置換
    val replaceAll: String = matcher.replaceAll("サ")
    assert(replaceAll == "ウサギはウサギだ。")
  }

  @Test
  def testUpdated(): Unit = {
    val index: Int = 6
    val replacement: Char = 'ジ'
    val result: String = tautology.updated(index, replacement)

    //インデックスが6の位置にある「ギ」が「ジ」に変換されています。
    assert(tautology == "ウナギはウナギだ。")
    assert(result    == "ウナギはウナジだ。")
  }

  @Test
  def testPatch(): Unit = {
    val index: Int = 4
    val offset: Int = 3
    val replacement: String = "イヌ"
    val result: String = tautology.patch(index, replacement, offset)

    //インデックスが4の位置にある「ウ」から３文字分の「ウナギ」が「イヌ」に変換されています。
    assert(tautology == "ウナギはウナギだ。")
    assert(result    == "ウナギはイヌだ。")
  }

  @Test
  def testCollectFirst(): Unit = {
    val resultOpt: Option[Char] = tautology collectFirst {
      case 'ギ' => 'コ'
      case 'ア' => 'ナ'
      case 'ナ' => 'ン'
      case '高' => 'く'
      case '地' => 'ん'
    }

    assert(tautology == "ウナギはウナギだ。")
    assert(resultOpt.nonEmpty)
    //２文字目の「ナ」に最初にマッチするので「ナ」の変更先の「ン」がresultOptに格納されています。
    assert(resultOpt.get == 'ン')
  }

  @Test
  def testCollect(): Unit = {
    val result: String = tautology collect {
      case 'ギ' => 'コ'
      case 'ア' => 'ナ'
      case 'ナ' => 'ン'
      case '高' => 'く'
      case '地' => 'ん'
      case otherwise => otherwise
    }

    //「ナ」が「ン」に、「ギ」が「コ」変わっています。
    assert(tautology == "ウナギはウナギだ。")
    assert(result    == "ウンコはウンコだ。")
  }

  @Test
  def testAlphabeticalOrder(): Unit = {
    assert(!('A' < 'A'))
    assert('A' < 'B')
    assert(!('B' < 'A'))
    assert(!('B' < 'B'))

    assert('A' <= 'A')
    assert('A' <= 'B')
    assert('B' <= 'B')
    assert(!('B' <= 'A'))

    assert('A'.compare('A') == 0)
    assert('A'.compare('B') < 0)
    assert('B'.compare('A') > 0)
    assert('B'.compare('B') == 0)

    assert('A'.compareTo('A') == 0)
    assert('A'.compareTo('B') < 0)
    assert('B'.compareTo('A') > 0)
    assert('B'.compareTo('B') == 0)

    assert(!("AA" < "AA"))
    assert("AA" < "AB")
    assert(!("AB" < "AA"))
    assert(!("AB" < "AB"))

    assert("AA" <= "AA")
    assert("AA" <= "AB")
    assert(!("AB" <= "AA"))
    assert("AB" <= "AB")

    assert("AA".compare("AA") == 0)
    assert("AA".compare("AB") < 0)
    assert("AB".compare("AA") > 0)
    assert("AB".compare("AB") == 0)

    assert("AA".compareTo("AA") == 0)
    assert("AA".compareTo("AB") < 0)
    assert("AB".compareTo("AA") > 0)
    assert("AB".compareTo("AB") == 0)

    assert("B" < "BA")
    assert(!("BA" < "B"))

    assert("B" <= "BA")
    assert(!("BA" <= "B"))

    assert("B".compare("BA") < 0)
    assert("BA".compare("B") > 0)

    assert("B".compareTo("BA") < 0)
    assert("BA".compareTo("B") > 0)

    //case
    assert("A".compareTo("a") < 0)
    assert("a".compareTo("A") > 0)

    assert("A".compareToIgnoreCase("a") == 0)
    assert("a".compareToIgnoreCase("A") == 0)
  }

  @Test
  def testNthCharInString1(): Unit = {
    //Char
    assert(tautology(3) == 'は')
  }

  @Test
  def testNthCharInString2(): Unit = {
    //Char
    assert(tautology.apply(3) == 'は')
  }

  @Test
  def testNthCharInString3(): Unit = {
    //Char
    assert(tautology.charAt(3) == 'は')
  }

  @Test
  def testNthCharInString4(): Unit = {
    //Char
    assert(tautology.applyOrElse(3, (index: Int) => 'を') == 'は')
    assert(tautology.applyOrElse(20, (index: Int) => s"${index}は${tautology.length}以上なのでCharは存在しません。")
      == "20は9以上なのでCharは存在しません。")
  }

  @Test
  def testNthCodePointInString(): Unit = {
    //コードポイント
    assert(tautology.codePointAt(3) == 'は')
  }

  @Test
  def testSubstring(): Unit = {
    assert(gardenPathSentence == "次郎は花子に渡した\nウナギを呼びつけた。")
    assert(gardenPathSentence.substring(10, 13) == "ウナギ")
  }

  @Test
  def testTakeHeadChar(): Unit = {
    assert(unagiCopula.head == '僕')
  }

  @Test
  def testTakeHeadCharOption(): Unit = {
    unagiCopula.headOption match {
      case Some(head) if head == '僕' =>
        assert(true)
      case otherwise =>
        assert(false)
    }
  }

  @Test
  def testTakeHeadString1(): Unit = {
    val numOfChars: Int = 3
    val result: String = tautology.take(numOfChars)
    assert(result == "ウナギ")
  }

  @Test
  def testTakeHeadString2(): Unit = {
    val numOfChars: Int = 3
    val result: String = tautology.substring(0, numOfChars)
    assert(result == "ウナギ")
  }

  @Test
  def testTakeHeadString3(): Unit = {
    val numOfChars: Int = 3
    val result: String = tautology.dropRight(tautology.length - numOfChars)
    assert(result == "ウナギ")
  }

  @Test
  def testTakeLastChar(): Unit = {
    assert(unagiCopula.last == 'ギ')
  }

  @Test
  def testTakeLastCharOption(): Unit = {
    unagiCopula.lastOption match {
      case Some(last) if last == 'ギ' =>
        assert(true)
      case otherwise =>
        assert(false)
    }
  }

  @Test
  def testTakeLastString1(): Unit = {
    val numOfChars: Int = 3
    val result: String = tautology.takeRight(numOfChars)
    assert(result == "ギだ。")
  }

  @Test
  def testTakeLastString2(): Unit = {
    val numOfChars: Int = 3
    val index: Int = tautology.length - numOfChars
    val result: String = tautology.substring(index)
    assert(result == "ギだ。")
  }

  @Test
  def testTakeLastString3(): Unit = {
    val numOfChars: Int = 3
    val index: Int = tautology.length - numOfChars
    val result: String = tautology.drop(index)
    assert(result == "ギだ。")
  }

  @Test
  def testTakeWhile(): Unit = {
    val result: String = tautology takeWhile {
      char =>
        //「ア」以上のChar
        // カタカナはひらがなや句読点よりCharが大きいので、
        // ひらがなや句読点が来たら条件を満たさなくなります。
        'ア' <= char
    }

    //ひらがなの「は」はカタカナの「ア」より小さいので、
    //「は」までの文字列が出力されています。
    assert(tautology == "ウナギはウナギだ。")
    assert(result == "ウナギ")
  }

  @Test
  def testFilter(): Unit = {
    val result: String = tautology filter {
      char =>
        //「ア」以上のChar
        // カタカナはひらがなや句読点よりCharが大きいので、
        // ひらがなや句読点が来たら条件を満たさなくなります。
        'ア' <= char
    }

    //「ア」以上のCharであるカタカナがフィルターをパスして残ります。
    assert(tautology == "ウナギはウナギだ。")
    assert(result == "ウナギウナギ")
  }

  @Test
  def testWithFilter(): Unit = {
    val result: String = tautology withFilter {
      char =>
        //「ア」以上のChar
        // カタカナはひらがなや句読点よりCharが大きいので、
        // ひらがなや句読点が来たら条件を満たさなくなります。
        'ア' <= char
    } map (char => char)

    //「ア」以上のCharであるカタカナがフィルターをパスして残ります。
    assert(tautology == "ウナギはウナギだ。")
    assert(result == "ウナギウナギ")
  }

  @Test
  def testFind(): Unit = {
    val resultOpt: Option[Char] = tautology find {
      char =>
        //「ア」以上のChar
        // カタカナはひらがなや句読点よりCharが大きいので、
        // ひらがなや句読点が来たら条件を満たします。
        char < 'ア'
    }

    assert(tautology == "ウナギはウナギだ。")
    assert(resultOpt.nonEmpty)
    //条件を満たしたひらがなの「は」が格納されています。
    assert(resultOpt.get == 'は')
  }

  @Test
  def testPartition(): Unit = {
    val pair1: (String, String) = tautology partition {
      char =>
        //「ア」以上のChar
        // カタカナはひらがなや句読点よりCharが大きいので、
        // ひらがなや句読点が来たら条件を満たさなくなります。
        'ア' <= char
    }

    //カタカナの「ア」以上の文字が連結した文字のみで構成された文字列
    assert(pair1._1 == "ウナギウナギ")
    //カタカナの「ア」未満の文字が連結した文字のみで構成された文字列
    assert(pair1._2 == "はだ。")

    val pair2: (String, String) = tautology partition {
      char =>
        //「ア」以上のChar
        // カタカナはひらがなや句読点よりCharが大きいので、
        // ひらがなや句読点が来たら条件を満たします。
        char < 'ア'
    }

    //カタカナの「ア」未満の文字が連結した文字のみで構成された文字列
    assert(pair2._1 == "はだ。")
    //カタカナの「ア」以上の文字が連結した文字のみで構成された文字列
    assert(pair2._2 == "ウナギウナギ")
  }

  @Test
  def testDeleteNthCharInString(): Unit = {
    val n: Int = 3
    val builder: StringBuilder = new StringBuilder(tautology.length).append(tautology)
    builder.deleteCharAt(n)
    val str: String = builder.result
    assert(str == "ウナギウナギだ。")
  }

  @Test
  def testDeleteSubstringInString(): Unit = {
    val builder: StringBuilder = new StringBuilder(tautology.length).append(tautology)
    builder.delete(3, 7)
    val str: String = builder.result
    assert(str == "ウナギだ。")
  }

  @Test
  def testDropHeadChar(): Unit = {
    assert(unagiCopula.tail == "はウナギ")
  }

  @Test
  def testStripPrefix(): Unit = {
    assert("横浜国立大学".stripPrefix("横浜") == "国立大学")
    unagiCopula.stringPrefix
  }

  @Test
  def testDropHeadString1(): Unit = {
    val numOfChars: Int = 3
    val result: String = tautology.drop(numOfChars)

    //先頭の3文字が除去されています。
    assert(tautology == "ウナギはウナギだ。")
    assert(result    == "はウナギだ。")
  }

  @Test
  def testDropHeadString2(): Unit = {
    val numOfChars: Int = 3
    val result: String = tautology.substring(numOfChars)

    //先頭の3文字が除去されています。
    assert(tautology == "ウナギはウナギだ。")
    assert(result    == "はウナギだ。")
  }

  @Test
  def testDropHeadString3(): Unit = {
    val numOfChars: Int = 3
    val result: String = tautology.takeRight(tautology.length - numOfChars)

    //先頭の3文字が除去されています。
    assert(tautology == "ウナギはウナギだ。")
    assert(result    == "はウナギだ。")
  }

  @Test
  def testDropLastChar(): Unit = {
    assert(unagiCopula.init == "僕はウナ")
  }

  @Test
  def testStripSuffix(): Unit = {
    assert("横浜国立大学".stripSuffix("立大学") == "横浜国")
  }

  @Test
  def testDropLastString1(): Unit = {
    val numOfChars: Int = 3
    val result: String = tautology.dropRight(numOfChars)

    //末尾の３文字が削除されています。
    assert(tautology == "ウナギはウナギだ。")
    assert(result    == "ウナギはウナ")
  }

  @Test
  def testDropLastString2(): Unit = {
    val numOfChars: Int = 3
    val result: String = tautology.substring(0, tautology.length - numOfChars)

    //末尾の３文字が削除されています。
    assert(tautology == "ウナギはウナギだ。")
    assert(result    == "ウナギはウナ")
  }

  @Test
  def testDropLastString3(): Unit = {
    val numOfChars: Int = 3
    val result: String = tautology.take(tautology.length - numOfChars)

    //末尾の３文字が削除されています。
    assert(tautology == "ウナギはウナギだ。")
    assert(result    == "ウナギはウナ")
  }

  @Test
  def testDeleteCharInString(): Unit = {
    assert(tautology.diff("ウ")            == "ナギはウナギだ。")
    assert(tautology.diff("ウ").diff("ウ") == "ナギはナギだ。")
    assert(tautology.diff("ウウ")          == "ナギはナギだ。")
  }

  @Test
  def testTrim(): Unit = {
    assert(gardenPathSentence  == "次郎は花子に渡した\nウナギを呼びつけた。")
    assert(gardenPathSentence2 == " 次郎は花子に渡した\nウナギを呼びつけた。\n\n    \r")
    assert(gardenPathSentence  == gardenPathSentence2.trim)
  }

  @Test
  def testStripLineEnd(): Unit = {
    //末尾の改行文字\nをひとつ削除
    assert("\nUnigram\nBigram\r\nTrigram\n".stripLineEnd   == "\nUnigram\nBigram\r\nTrigram")
    assert("\nUnigram\nBigram\r\nTrigram\n\n".stripLineEnd == "\nUnigram\nBigram\r\nTrigram\n")

    //末尾の改行文字\r\nをひとつ削除
    assert("\nUnigram\nBigram\r\nTrigram\r\n".stripLineEnd     == "\nUnigram\nBigram\r\nTrigram")
    assert("\nUnigram\nBigram\r\nTrigram\r\n\r\n".stripLineEnd == "\nUnigram\nBigram\r\nTrigram\r\n")

    //\rや\n\rは改行文字ではないので削除されません
    assert("\nUnigram\nBigram\r\nTrigram\n\r".stripLineEnd  == "\nUnigram\nBigram\r\nTrigram\n\r")

    //半角スペースも削除されません
    assert("\nUnigram\nBigram\r\nTrigram\n\n ".stripLineEnd == "\nUnigram\nBigram\r\nTrigram\n\n ")
  }

  @Test
  def testDropWhile(): Unit = {
    val result: String = tautology dropWhile {
      char =>
        //「ア」以上のChar
        // カタカナはひらがなや句読点よりCharが大きいので、
        // ひらがなや句読点が来たら条件を満たさなくなります。
        'ア' <= char
    }

    //ひらがなの「は」はカタカナの「ア」より小さいので、
    //「は」までの文字列が除去されています。
    assert(tautology == "ウナギはウナギだ。")
    assert(result == "はウナギだ。")
  }

  @Test
  def testFilterNot(): Unit = {
    val result: String = tautology filterNot {
      char =>
        //「ア」以上のChar
        // カタカナはひらがなや句読点よりCharが大きいので、
        // ひらがなや句読点が来たら条件を満たさなくなります。
        'ア' <= char
    }

    //「ア」以上のCharであるカタカナがフィルターでカットされてひらがなが残ります。
    assert(tautology == "ウナギはウナギだ。")
    assert(result == "はだ。")
  }

  @Test
  def testStripMargin(): Unit = {
    assert("""
ABC
DEF
GHI
""" == "\nABC\nDEF\nGHI\n")

//    assert("""
//ここはなし |ABC
//ここは無視|DEF
//これは無視|GHI
//""".stripMargin == "\nABC\nDEF\nGHI\n")

    assert("""
  %ABC
  %DEF
  %GHI
""".stripMargin('%') == "\nABC\nDEF\nGHI\n")
  }

  private val list: Seq[String] = Seq[String](
    "A", "B", "C", "D", "E", "F",
    "A", "AB", "AA", "BA",
    "a", "b", "c", "d", "e", "f",
    "あ", "い", "う", "え", "お",
    "ア", "イ", "ウ", "エ", "オ",
    "安", "以", "宇", "衣", "於",
    "。", "、", "．", "，", "𠮷"
  )

  @Test
  def testSort1(): Unit = {
    assert(list.sorted == Seq[String](
      "A", "A", "AA", "AB", "B", "BA", "C", "D", "E", "F",
      "a", "b", "c", "d", "e", "f",
      "、", "。",
      "あ", "い", "う", "え", "お",
      "ア", "イ", "ウ", "エ", "オ",
      "以", "宇", "安", "於", "衣",
      "𠮷", "，", "．"
    ))
  }

  @Test
  def testSort2(): Unit = {
    assert(list.sortWith((a, b) => a < b) == Seq[String](
      "A", "A", "AA", "AB", "B", "BA", "C", "D", "E", "F",
      "a", "b", "c", "d", "e", "f",
      "、", "。",
      "あ", "い", "う", "え", "お",
      "ア", "イ", "ウ", "エ", "オ",
      "以", "宇", "安", "於", "衣",
      "𠮷", "，", "．"
    ))

    assert(list.sortWith(_ < _) == Seq[String](
      "A", "A", "AA", "AB", "B", "BA", "C", "D", "E", "F",
      "a", "b", "c", "d", "e", "f",
      "、", "。",
      "あ", "い", "う", "え", "お",
      "ア", "イ", "ウ", "エ", "オ",
      "以", "宇", "安", "於", "衣",
      "𠮷", "，", "．"
    ))
  }

  @Test
  def testSort3(): Unit = {
    assert(list.sortWith((a, b) => a <= b) == Seq[String](
      "A", "A", "AA", "AB", "B", "BA", "C", "D", "E", "F",
      "a", "b", "c", "d", "e", "f",
      "、", "。",
      "あ", "い", "う", "え", "お",
      "ア", "イ", "ウ", "エ", "オ",
      "以", "宇", "安", "於", "衣",
      "𠮷", "，", "．"
    ))

    assert(list.sortWith(_ <= _) == Seq[String](
      "A", "A", "AA", "AB", "B", "BA", "C", "D", "E", "F",
      "a", "b", "c", "d", "e", "f",
      "、", "。",
      "あ", "い", "う", "え", "お",
      "ア", "イ", "ウ", "エ", "オ",
      "以", "宇", "安", "於", "衣",
      "𠮷", "，", "．"
    ))
  }

  @Test
  def testSort4(): Unit = {
    assert(list.sortBy(transform) == Seq[String](
      "A", "B", "C", "D", "E", "F", "A",
      "a", "b", "c", "d", "e", "f",
      "あ", "い", "う", "え", "お",
      "ア", "イ", "ウ", "エ", "オ",
      "安", "以", "宇", "衣", "於",
      "。", "、", "．", "，",
      "AB", "AA", "BA", "𠮷"
    ))

    //ここで変換されたものに従って並べることができます
    def transform(s: String): Int = {
      s.length
    }
  }

  @Test
  def testReverse1(): Unit = {
    assert(list.reverse == Seq[String](
      "𠮷", "，", "．", "、", "。",
      "於", "衣", "宇", "以", "安",
      "オ", "エ", "ウ", "イ", "ア",
      "お", "え", "う", "い", "あ",
      "f", "e", "d", "c", "b", "a",
      "BA", "AA", "AB", "A", "F", "E", "D", "C", "B", "A"
    ))
  }

  @Test
  def testReverse2(): Unit = {
    assert(list.sorted.reverse == Seq[String](
      "．", "，", "𠮷",
      "衣", "於", "安", "宇", "以",
      "オ", "エ", "ウ", "イ", "ア",
      "お", "え", "う", "い", "あ",
      "。", "、",
      "f", "e", "d", "c", "b", "a",
      "F", "E", "D", "C", "BA", "B", "AB", "AA", "A", "A"
    ))

    assert(list.reverse.sorted == Seq[String](
      "A", "A", "AA", "AB", "B", "BA", "C", "D", "E", "F",
      "a", "b", "c", "d", "e", "f",
      "、", "。",
      "あ", "い", "う", "え", "お",
      "ア", "イ", "ウ", "エ", "オ",
      "以", "宇", "安", "於", "衣",
      "𠮷", "，", "．"
    ))
  }

  @Test
  def testReverse3(): Unit = {
    assert(list.sortWith((a, b) => a > b) == Seq[String](
      "．", "，", "𠮷",
      "衣", "於", "安", "宇", "以",
      "オ", "エ", "ウ", "イ", "ア",
      "お", "え", "う", "い", "あ",
      "。", "、",
      "f", "e", "d", "c", "b", "a",
      "F", "E", "D", "C", "BA", "B", "AB", "AA", "A", "A"
    ))

    assert(list.sortWith(_ > _) == Seq[String](
      "．", "，", "𠮷",
      "衣", "於", "安", "宇", "以",
      "オ", "エ", "ウ", "イ", "ア",
      "お", "え", "う", "い", "あ",
      "。", "、",
      "f", "e", "d", "c", "b", "a",
      "F", "E", "D", "C", "BA", "B", "AB", "AA", "A", "A"
    ))
  }

  @Test
  def testReverse4(): Unit = {
    assert(list.sortWith((a, b) => a >= b) == Seq[String](
      "．", "，", "𠮷",
      "衣", "於", "安", "宇", "以",
      "オ", "エ", "ウ", "イ", "ア",
      "お", "え", "う", "い", "あ",
      "。", "、",
      "f", "e", "d", "c", "b", "a",
      "F", "E", "D", "C", "BA", "B", "AB", "AA", "A", "A"
    ))

    assert(list.sortWith(_ >= _) == Seq[String](
      "．", "，", "𠮷",
      "衣", "於", "安", "宇", "以",
      "オ", "エ", "ウ", "イ", "ア",
      "お", "え", "う", "い", "あ",
      "。", "、",
      "f", "e", "d", "c", "b", "a",
      "F", "E", "D", "C", "BA", "B", "AB", "AA", "A", "A"
    ))
  }

  @Test
  def testSortChars(): Unit = {
    assert("𠮷" == "\uD842\uDFB7")

    val str: String = "安衣宇以於\uD842\uDFB7"
    assert(str.sorted == "以宇安於衣\uD842\uDFB7")
    assert(str.reverse != "\uD842\uDFB7於以宇衣安")
    assert(str.reverse == "\uDFB7\uD842於以宇衣安")

    val builder: StringBuilder = new StringBuilder(str)
    val reverse: String = builder.reverse.result
    assert(reverse == "\uD842\uDFB7於以宇衣安")
    assert(reverse != "\uDFB7\uD842於以宇衣安")
  }

  @Test
  def testMax(): Unit = {
    val str: String = "安衣宇以於\uD842\uDFB7"
    val list: Seq[String] = Seq[String]("安", "衣", "宇", "以", "於", "𠮷")

    assert(str.max == '\uDFB7')
    assert(list.max == "𠮷")
  }

  @Test
  def testMaxBy(): Unit = {
    val str: String = "安衣宇以於\uD842\uDFB7"
    val list: Seq[String] = Seq[String]("安", "衣", "宇", "以", "於", "𠮷")

    assert(str.maxBy(transformChar) == '\uDFB7')
    assert(list.maxBy(transformString) == "𠮷")

    //ここで変換されたものに従って並べることができます
    def transformChar(char: Char): Char = {
      char
    }

    //ここで変換されたものに従って並べることができます
    def transformString(str: String): Int = {
      str.length
    }
  }

  @Test
  def testMin(): Unit = {
    val str: String = "安衣宇以於\uD842\uDFB7"
    val list: Seq[String] = Seq[String]("安", "衣", "宇", "以", "於", "𠮷")

    assert(str.min == '以')
    assert(list.min == "以")
  }

  @Test
  def testMinBy(): Unit = {
    val str: String = "安衣宇以於\uD842\uDFB7"
    val list: Seq[String] = Seq[String]("安", "衣", "宇", "以", "於", "𠮷")

    assert(str.minBy(transformChar) == '以')
    assert(list.minBy(transformString) == "安")

    //ここで変換されたものに従って並べることができます
    def transformChar(char: Char): Char = {
      char
    }

    //ここで変換されたものに従って並べることができます
    def transformString(str: String): Int = {
      str.length
    }
  }

  @Test
  def testSum(): Unit = {
    val str: String = "安衣宇以於\uD842\uDFB7"

    //Charは整数型なので総和が計算でわかりますが、なんのために使うのかはわかりません
    assert(str.sum == '갍')

    //val list: Seq[String] = Seq[String]("安", "衣", "宇", "以", "於", "𠮷")

    //Stringは整数型ではないので総和が計算できません
    //println(list.sum)
  }

  @Test
  def testProduct(): Unit = {
    val str: String = "安衣宇以於\uD842\uDFB7"

    //Charは整数型なので総乗が計算でわかりますが、なんのために使うのかはわかりません
    assert(str.product == 'ㅈ')

    //val list: Seq[String] = Seq[String]("安", "衣", "宇", "以", "於", "𠮷")

    //Stringは整数型ではないので総乗が計算できません
    //println(list.product)
  }

  @Test
  def testUnion(): Unit = {
    val str1: String = "安衣宇"
    val str2: String = "以於\uD842\uDFB7"

    assert(str1.union(str2) == "安衣宇以於𠮷")

    val list1: Seq[String] = Seq[String]("安", "衣", "宇")
    val list2: Seq[String] = Seq[String]("以", "於", "𠮷")

    assert(list1.union(list2) == Seq[String]("安", "衣", "宇", "以", "於", "𠮷"))
  }

  @Test
  def testDiff(): Unit = {
    assert(tautology == "ウナギはウナギだ。")

    assert(tautology.diff("ウ")            == "ナギはウナギだ。")
    assert(tautology.diff("ウ").diff("ウ") == "ナギはナギだ。")
    assert(tautology.diff("ウウ")          == "ナギはナギだ。")

    val list: Seq[String] = Seq[String]("安", "衣", "安", "安", "宇", "以", "於", "𠮷")

    assert(list.diff(Seq[String]("安"))
      == Seq[String]("衣", "安", "安", "宇", "以", "於", "𠮷"))
    assert(list.diff(Seq[String]("安")).diff(Seq[String]("安"))
      == Seq[String]("衣", "安", "宇", "以", "於", "𠮷"))
    assert(list.diff(Seq[String]("安", "安"))
      == Seq[String]("衣", "安", "宇", "以", "於", "𠮷"))
  }

  @Test
  def testDistinct(): Unit = {
    assert(tautology == "ウナギはウナギだ。")
    assert(tautology.distinct == "ウナギはだ。")

    val list: Seq[String] = Seq[String]("安", "衣", "安", "安", "宇", "以", "於", "𠮷")
    assert(list.distinct == Seq[String]("安", "衣", "宇", "以", "於", "𠮷"))
  }

  @Test
  def testIntersect(): Unit = {
    val str1: String = "$ウ$ナ$ギ$は"
    val str2: String = "ウ#ナ#ギ#だ#。#"
    assert(str1.intersect(str2) == "ウナギ")

    val list1: Seq[String] = Seq[String]("安", "衣", "安", "安", "宇", "以", "於", "𠮷")
    val list2: Seq[String] = Seq[String]("衣", "う", "お", "𠮷", "安", "安", "え", "あ")
    assert(list1.intersect(list2) == Seq[String]("安", "衣", "安", "𠮷"))
  }

  @Test
  def testCombinations(): Unit = {
    val str: String = "𠮷野家"

    val list: Seq[String] = Seq[String]("𠮷", "野", "家")

    val strCombo: ListBuffer[String] = ListBuffer[String]()
    str.combinations(2) foreach {strCombo.+=}
    assert(strCombo == Seq[String]("𠮷", "\uD842野", "\uD842家", "\uDFB7野", "\uDFB7家", "野家"))

    val listCombo: ListBuffer[Seq[String]] = ListBuffer[Seq[String]]()
    list.combinations(2) foreach {listCombo.+=}
    assert(listCombo == Seq[Seq[String]](Seq("𠮷", "野"), Seq("𠮷", "家"), Seq("野", "家")))
  }

  @Test
  def testPermutations(): Unit = {
    val str: String = "𠮷野家"

    val list: Seq[String] = Seq[String]("𠮷", "野", "家")

    val strPerm: ListBuffer[String] = ListBuffer[String]()
    str.permutations foreach {strPerm.+=}
    assert(strPerm == Seq[String](
      "𠮷野家", "𠮷家野", "\uD842野\uDFB7家", "\uD842野家\uDFB7", "\uD842家\uDFB7野", "\uD842家野\uDFB7",
      "\uDFB7\uD842野家", "\uDFB7\uD842家野", "\uDFB7野\uD842家", "\uDFB7野家\uD842", "\uDFB7家\uD842野", "\uDFB7家野\uD842",
      "野𠮷家", "野\uD842家\uDFB7", "野\uDFB7\uD842家", "野\uDFB7家\uD842", "野家𠮷", "野家\uDFB7\uD842",
      "家𠮷野", "家\uD842野\uDFB7", "家\uDFB7\uD842野", "家\uDFB7野\uD842", "家野𠮷", "家野\uDFB7\uD842"))

    val listPerm: ListBuffer[Seq[String]] = ListBuffer[Seq[String]]()
    list.permutations foreach {listPerm.+=}
    assert(listPerm == Seq[Seq[String]](
      Seq("𠮷", "野", "家"), Seq("𠮷", "家", "野"),
      Seq("野", "𠮷", "家"), Seq("野", "家", "𠮷"),
      Seq("家", "𠮷", "野"), Seq("家", "野", "𠮷")))
  }

  @Test
  def testIndexOf1(): Unit = {
    assert(tautology.indexOf('ギ') == 2)
  }

  @Test
  def testIndexOf2(): Unit = {
    assert(tautology.indexOf('ギ', 3) == 6)
  }

  @Test
  def testIndexOf3(): Unit = {
    assert(tautology.indexOf("ナギ")      == 1)
    assert(tautology.indexOfSlice("ナギ") == 1)
  }

  @Test
  def testIndexOf4(): Unit = {
    assert(tautology.indexOf("ナギ", 3)      == 5)
    assert(tautology.indexOfSlice("ナギ", 3) == 5)
  }

  @Test
  def testLastIndexOf1(): Unit = {
    assert(tautology.lastIndexOf('ギ') == 6)
  }

  @Test
  def testLastIndexOf2(): Unit = {
    assert(tautology.lastIndexOf('ギ', 3) == 2)
  }

  @Test
  def testLastIndexOf3(): Unit = {
    assert(tautology.lastIndexOf("ナギ")      == 5)
    assert(tautology.lastIndexOfSlice("ナギ") == 5)
  }

  @Test
  def testLastIndexOf4(): Unit = {
    assert(tautology.lastIndexOf("ナギ", 3)      == 1)
    assert(tautology.lastIndexOfSlice("ナギ", 3) == 1)
  }

  @Test
  def testPrefixLength(): Unit = {
    assert(tautology.prefixLength(where) == 2)

    def where(char: Char): Boolean = {
      char != 'ギ'
    }
  }

  @Test
  def testSegmentLength(): Unit = {
    assert(tautology.segmentLength(where, 1) == 1)

    def where(char: Char): Boolean = {
      char != 'ギ'
    }
  }

  @Test
  def testIndexWhere1(): Unit = {
    assert(tautology.indexWhere(where) == 2)

    def where(char: Char): Boolean = {
      char == 'ギ'
    }
  }

  @Test
  def testIndexWhere2(): Unit = {
    assert(tautology.indexWhere(where, 3) == 6)

    def where(char: Char): Boolean = {
      char == 'ギ'
    }
  }

  @Test
  def testLastIndexWhere1(): Unit = {
    assert(tautology.lastIndexWhere(where) == 6)

    def where(char: Char): Boolean = {
      char == 'ギ'
    }
  }

  @Test
  def testLastIndexWhere2(): Unit = {
    assert(tautology.lastIndexWhere(where, 3) == 2)

    def where(char: Char): Boolean = {
      char == 'ギ'
    }
  }

  @Test
  def testIndices(): Unit = {
    val indices: Range = tautology.indices
    assert(indices.nonEmpty)
    assert(indices.start == 0)
    assert(indices.end   == 9)
    assert(indices.step  == 1)
    assert(indices == Seq[Int](0, 1, 2, 3, 4, 5, 6, 7, 8))
  }

  @Test
  def testZipWithIndex(): Unit = {
    assert(tautology.zipWithIndex == Seq[(Char, Int)](
      ('ウ', 0), ('ナ', 1), ('ギ', 2), ('は', 3),
      ('ウ', 4), ('ナ', 5), ('ギ', 6), ('だ', 7), ('。', 8)))
  }

  @Test
  def testIsDefinedAt(): Unit = {
    assert(!tautology.isDefinedAt(-1))
    assert(tautology.isDefinedAt(0))
    assert(tautology.isDefinedAt(tautology.length - 1))
    assert(!tautology.isDefinedAt(tautology.length))
  }

  @Test
  def testForeach(): Unit = {
    val result1: ListBuffer[Char] = ListBuffer[Char]()
    unagiCopula foreach result1.+=

    assert(result1 == Seq[Char]('僕', 'は', 'ウ', 'ナ', 'ギ'))

    val result2: ListBuffer[Char] = ListBuffer[Char]()
    unagiCopula foreach {
      char =>
        result2 += char
    }

    assert(result2 == Seq[Char]('僕', 'は', 'ウ', 'ナ', 'ギ'))
  }

  @Test
  def testCharacterIterator(): Unit = {
    val result: ListBuffer[Char] = ListBuffer[Char]()
    val iterator: CharacterIterator = new StringCharacterIterator(unagiCopula)
    var char: Char = iterator.first
    while (char != CharacterIterator.DONE) {
      result += char
      char = iterator.next
    }
    assert(result == Seq[Char]('僕', 'は', 'ウ', 'ナ', 'ギ'))
  }

  @Test
  def testIterator(): Unit = {
    assert(unagiCopula.iterator.toSeq == Seq[Char]('僕', 'は', 'ウ', 'ナ', 'ギ'))
  }

  @Test
  def testReverseIterator(): Unit = {
    assert(unagiCopula.reverseIterator.toSeq == Seq[Char]('ギ', 'ナ', 'ウ', 'は', '僕'))
  }

  @Test
  def testMap(): Unit = {
    assert(unagiCopula.map(char => char) == "僕はウナギ")
  }

  @Test
  def testReverseMap(): Unit = {
    assert(unagiCopula.reverseMap(char => char) == "ギナウは僕")
  }

  @Test
  def testSliding1(): Unit = {
    val n: Int = 2
    val nGrams: ListBuffer[String] = ListBuffer[String]()
    for (nGram <- unagiCopula.sliding(n)) {
      nGrams += nGram
    }

    assert(nGrams == Seq[String]("僕は", "はウ", "ウナ", "ナギ"))
  }

  @Test
  def testSliding2(): Unit = {
    val n: Int = 2

    val nGrams1: ListBuffer[String] = ListBuffer[String]()
    for (nGram <- unagiCopula.sliding(n, 1)) {
      nGrams1 += nGram
    }

    assert(nGrams1 == Seq[String]("僕は", "はウ", "ウナ", "ナギ"))

    val nGrams2: ListBuffer[String] = ListBuffer[String]()
    for (nGram <- unagiCopula.sliding(n, 2)) {
      nGrams2 += nGram
    }

    assert(nGrams2 == Seq[String]("僕は", "ウナ", "ギ"))

    val nGrams3: ListBuffer[String] = ListBuffer[String]()
    for (nGram <- unagiCopula.sliding(n, 3)) {
      nGrams3 += nGram
    }

    assert(nGrams3 == Seq[String]("僕は", "ナギ"))

    val nGrams4: ListBuffer[String] = ListBuffer[String]()
    for (nGram <- unagiCopula.sliding(n, 4)) {
      nGrams4 += nGram
    }

    assert(nGrams4 == Seq[String]("僕は", "ギ"))

    val nGrams5: ListBuffer[String] = ListBuffer[String]()
    for (nGram <- unagiCopula.sliding(n, 5)) {
      nGrams5 += nGram
    }

    assert(nGrams5 == Seq[String]("僕は"))
  }

  @Test
  def testGrouped(): Unit = {
    val n: Int = 2
    val groups: ListBuffer[String] = ListBuffer[String]()
    for (group <- unagiCopula.grouped(n)) {
      groups += group
    }

    assert(groups == Seq[String]("僕は", "ウナ", "ギ"))
  }

  @Test
  def testLines(): Unit = {
    val str: String = "\nUnigram\nBigram\r\nTrigram\n\n"
    val result: ListBuffer[String] = ListBuffer[String]()
    for (line <- str.lines) {
      result += line
    }
    assert(result == Seq[String]("", "Unigram", "Bigram", "Trigram", ""))
  }

  @Test
  def testLinesWithSeparators(): Unit = {
    val str: String = "\nUnigram\nBigram\r\nTrigram\n\n"
    val result: ListBuffer[String] = ListBuffer[String]()
    for (line <- str.linesWithSeparators) {
      result += line
    }
    assert(result == Seq[String]("\n", "Unigram\n", "Bigram\r\n", "Trigram\n", "\n"))
  }

  private def divide(numerator: Double, denominator: Double): Double = {
    if (denominator == 0) {
      return 0D
    }
    numerator / denominator
  }

  @Test
  def testLCSBasedF1(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    val codePointsOfSource: Array[Int] = source.codePoints.toArray
    val codePointsOfTarget: Array[Int] = target.codePoints.toArray

    val lengthOfSource: Int = codePointsOfSource.length
    val lengthOfTarget: Int = codePointsOfTarget.length

    assert(lengthOfSource == 11)
    assert(lengthOfTarget == 13)

    val lcs: Array[Int] = codePointsOfSource.intersect(codePointsOfTarget)
    val lcsLength: Int = lcs.length

    assert(lcsLength == 6)
    assert(new String(lcs, 0, lcsLength) == "ウウナナギギ")

    val recall: Double = divide(lcsLength, lengthOfSource)

    val precision: Double = divide(lcsLength, lengthOfTarget)

    val f1: Double = divide(recall * precision * 2, recall + precision)

    assert(recall    == 0.5454545454545454D)
    assert(precision == 0.46153846153846156D)
    assert(f1        == 0.4999999999999999D)
  }

  @Test
  def testNGramBasedF1(): Unit = {
    val n: Int = 2

    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    val codePointsOfSource: Array[Int] = source.codePoints.toArray
    val codePointsOfTarget: Array[Int] = target.codePoints.toArray

    val codePointNGramsOfSource: Array[Array[Int]] = codePointsOfSource.sliding(n).toArray
    val codePointNGramsOfTarget: Array[Array[Int]] = codePointsOfTarget.sliding(n).toArray

    val nGramsOfSource: Array[String] = {
      codePointNGramsOfSource map {
        codePoints =>
          new String(codePoints, 0, codePoints.length)
      }
    }.distinct
    val nGramsOfTarget: Array[String] = {
      codePointNGramsOfTarget map {
        codePoints =>
          new String(codePoints, 0, codePoints.length)
      }
    }.distinct

    assert(nGramsOfSource sameElements Array[String]("$ウ", "ウウ", "ウ$", "$ナ", "ナナ", "ナ$", "$ギ", "ギギ", "ギ$", "$は"))
    assert(nGramsOfTarget sameElements Array[String]("ウウ", "ウ#", "#ナ", "ナナ", "ナ#", "#ギ", "ギギ", "ギ#", "#だ", "だ#", "#。", "。#"))

    val lengthOfSource: Int = nGramsOfSource.length
    val lengthOfTarget: Int = nGramsOfTarget.length

    assert(lengthOfSource == 10)
    assert(lengthOfTarget == 12)

    val lcs: Array[String] = nGramsOfSource.intersect(nGramsOfTarget)
    val lcsLength: Int = lcs.length

    assert(lcsLength == 3)
    assert(lcs sameElements Array[String]("ウウ", "ナナ", "ギギ"))

    val recall: Double = divide(lcsLength, lengthOfSource)

    val precision: Double = divide(lcsLength, lengthOfTarget)

    val f1: Double = divide(recall * precision * 2, recall + precision)

    assert(recall    == 0.3D)
    assert(precision == 0.25D)
    assert(f1        == 0.2727272727272727D)
  }

  @Test
  def testHammingDistance(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    assert(HammingDistance.calculate(source, target.substring(0, source.length)) == 0.7272727272727273D)
  }

  @Test
  def testLevenshteinDistance(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    assert(LevenshteinDistance.calculate(source, target) == 0.3846153846153846D)
  }

  @Test
  def testDamerauLevenshteinDistance(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    assert(DamerauLevenshteinDistance.calculate(source, target) == 0.3846153846153846D)
  }

  @Test
  def testJaroDistance(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    assert(JaroDistance.calculate(source, target) == 0.668997668997669D)
  }

  @Test
  def testJaroWinklerDistance(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    assert(JaroWinklerDistance.calculate(source, target) == 0.668997668997669D)
  }

  @Test
  def testF1WithLCS(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    assert(Overlap.calculateF1WithLCS(
      Tokenizer.tokenize(Option(source)),
      Tokenizer.tokenize(Option(target))) == 0.2727272727272727D)
  }

  @Test
  def testBagOfBigramsCosineSimilarity(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    val vector1: FrequencyVector = FrequencyVectorGenerator.getVectorFromText(source)
    val vector2: FrequencyVector = FrequencyVectorGenerator.getVectorFromText(target)

    assert(SimilarityCalculator.calculate(vector1, vector2) == 0.27386127875258304D)
  }

  @Test
  def testBagOfBigramsEuclideanDistance(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    val vector1: FrequencyVector = FrequencyVectorGenerator.getVectorFromText(source)
    val vector2: FrequencyVector = FrequencyVectorGenerator.getVectorFromText(target)

    assert(Dissimilarity.calculateEuclidean(vector1, vector2) == 4.0D)
  }

  @Test
  def testBigramsInclusion(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    val vector1: BinaryVector = BinaryVectorGenerator.getVectorFromText(source)
    val vector2: BinaryVector = BinaryVectorGenerator.getVectorFromText(target)

    assert(OverlapCalculator.calculate(vector1, vector2) == 0.3D)
  }
}
