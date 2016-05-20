# 2.　文字列操作
<h3>2.1　比較</h3>
文字列間の等値、表層文字列による一致や正規表現によるパターンマッチについては前章で説明しました。
ここでは、文字をアルファベット・あいうえお順（厳密にはChar順）に並べたる方法を説明します。

```scala
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

    assert(!("AA" < "AA"))
    assert("AA" < "AB")
    assert(!("AB" < "AA"))
    assert(!("AB" < "AB"))

    assert("AA" <= "AA")
    assert("AA" <= "AB")
    assert(!("AB" <= "AA"))
    assert("AB" <= "AB")

    assert("B" < "BA")
    assert(!("BA" < "B"))

    assert("B" <= "BA")
    assert(!("BA" <= "B"))
  }
```
---
<h3>2.2　パスフィルタ</h3>
<h4>2.2.1　N番目の文字の取得</h4>

```scala
  @Test
  def testNthCharInString1(): Unit = {
    //Char
    assert(tautology(3) == 'は')
  }

  @Test
  def testNthCharInString2(): Unit = {
    //Char
    assert(tautology.charAt(3) == 'は')
  }

  @Test
  def testNthCharInString3(): Unit = {
    //コードポイント
    assert(tautology.codePointAt(3) == 'は')
  }
```
<h4>2.2.2　部分文字列の取得</h4>

```scala
  @Test
  def testSubstring(): Unit = {
    assert(gardenPathSentence == "次郎は花子に渡した\nウナギを呼びつけた。")
    assert(gardenPathSentence.substring(10, 13) == "ウナギ")
  }
```
<h4>2.2.3　先頭の一文字の取得</h4>

```scala
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
```
<h4>2.2.4　先頭のN文字の取得</h4>
```scala
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
```
<h4>2.2.5　末尾の一文字の取得</h4>

```scala
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
```
<h4>2.2.6　末尾のN文字の取得</h4>

```scala
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
```
<h4>2.2.7　条件式を満たす文字や文字列の取得</h4>
```scala
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
```

```scala
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
```

```scala
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
```

```scala
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
```

---
<h3>2.3　カットフィルタ</h3>
<h4>2.3.1　N番目の文字の削除</h4>

```scala
  @Test
  def testDeleteNthCharInString(): Unit = {
    val n: Int = 3
    val builder: StringBuilder = new StringBuilder(tautology.length).append(tautology)
    builder.deleteCharAt(n)
    val str: String = builder.result
    assert(str == "ウナギウナギだ。")
  }
```
<h4>2.3.2　部分文字列の削除</h4>

```scala
  @Test
  def testDeleteSubstringInString(): Unit = {
    val builder: StringBuilder = new StringBuilder(tautology.length).append(tautology)
    builder.delete(3, 7)
    val str: String = builder.result
    assert(str == "ウナギだ。")
  }
```
<h4>2.3.3　先頭の一文字の削除</h4>
```scala
  @Test
  def testDropHeadChar(): Unit = {
    assert(unagiCopula.tail == "はウナギ")
  }
```
<h4>2.3.4　先頭のN文字の削除</h4>
```scala
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
```
<h4>2.3.5　先頭の文字列が一致したら削除</h4>
```scala
  @Test
  def testStripPrefix(): Unit = {
    assert("横浜国立大学".stripPrefix("横浜") == "国立大学")
    unagiCopula.stringPrefix
  }

```
<h4>2.3.6　末尾の一文字の削除</h4>
```scala
  @Test
  def testDropLastChar(): Unit = {
    assert(unagiCopula.init == "僕はウナ")
  }
```
<h4>2.3.7　末尾のN文字の削除</h4>
```scala
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
```

<h4>2.3.8　末尾の文字列が一致したら削除</h4>
```scala
  @Test
  def testStripSuffix(): Unit = {
    assert("横浜国立大学".stripSuffix("立大学") == "横浜国")
  }
```
<h4>2.3.9　先頭・末尾の空白文字削除</h4>
```scala
  @Test
  def testTrim(): Unit = {
    assert(gardenPathSentence  == "次郎は花子に渡した\nウナギを呼びつけた。")
    assert(gardenPathSentence2 == " 次郎は花子に渡した\nウナギを呼びつけた。\n\n    \r")
    assert(gardenPathSentence  == gardenPathSentence2.trim)
  }
```
<h4>2.3.10　末尾改行文字削除</h4>
```scala
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
```
<h4>2.3.11　条件式を満たす文字や文字列の削除</h4>
```scala
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
```

```scala
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
```

```scala
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
```
---
<h3>2.4　ソート</h3>
<h4>2.4.1　Charによる文字列リストの正順ソート</h4>
```scala
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
  }

  @Test
  def testSort4(): Unit = {
    assert(list.sortBy(transform) == Seq[String](
      "A", "A", "AA", "AB", "B", "BA", "C", "D", "E", "F",
      "a", "b", "c", "d", "e", "f",
      "、", "。",
      "あ", "い", "う", "え", "お",
      "ア", "イ", "ウ", "エ", "オ",
      "以", "宇", "安", "於", "衣",
      "𠮷", "，", "．"
    ))

    //ここで変換されたものに従って並べることができます
    def transform(s: String): String = {
      s
    }
  }
```
<h4>2.4.2　Charによる文字列リストの逆順ソート</h4>
```scala
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
  }
```
<h4>2.4.3　Charによる文字のソート</h4>
```scala
  @Test
  def testSortChars(): Unit = {
    assert("𠮷" == "\uD842\uDFB7")

    val str: String = "安衣宇以於\uD842\uDFB7"
    assert(str.sorted == "以宇安於衣\uD842\uDFB7")
    assert(str.reverse != "\uD842\uDFB7於以宇衣安")
    assert(str.reverse == "\uDFB7\uD842於以宇衣安")
  }
```
---
<h3>2.5　集合演算</h3>
<h4>2.5.1　max</h4>
```scala
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
    def transformString(str: String): String = {
      str
    }
  }
```
<h4>2.5.2　min</h4>
```scala
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
    assert(list.minBy(transformString) == "以")

    //ここで変換されたものに従って並べることができます
    def transformChar(char: Char): Char = {
      char
    }

    //ここで変換されたものに従って並べることができます
    def transformString(str: String): String = {
      str
    }
  }
```
<h4>2.5.3　sum</h4>
```scala
  @Test
  def testSum(): Unit = {
    val str: String = "安衣宇以於\uD842\uDFB7"

    //Charは整数型なので総和が計算でわかりますが、なんのために使うのかはわかりません
    assert(str.sum == '갍')

    //val list: Seq[String] = Seq[String]("安", "衣", "宇", "以", "於", "𠮷")

    //Stringは整数型ではないので総和が計算できません
    //println(list.sum)
  }
```
<h4>2.5.4　product</h4>
```scala
  @Test
  def testProduct(): Unit = {
    val str: String = "安衣宇以於\uD842\uDFB7"

    //Charは整数型なので総乗が計算でわかりますが、なんのために使うのかはわかりません
    assert(str.product == 'ㅈ')

    //val list: Seq[String] = Seq[String]("安", "衣", "宇", "以", "於", "𠮷")

    //Stringは整数型ではないので総乗が計算できません
    //println(list.product)
  }
```
<h4>2.5.5　union</h4>
```scala
  @Test
  def testUnion(): Unit = {
    val str1: String = "安衣宇"
    val str2: String = "以於\uD842\uDFB7"

    assert(str1.union(str2) == "安衣宇以於𠮷")

    val list1: Seq[String] = Seq[String]("安", "衣", "宇")
    val list2: Seq[String] = Seq[String]("以", "於", "𠮷")

    assert(list1.union(list2) == Seq[String]("安", "衣", "宇", "以", "於", "𠮷"))
  }
```
<h4>2.5.6　diff</h4>
```scala
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
```
<h4>2.5.7　distinct</h4>
```scala
  @Test
  def testDistinct(): Unit = {
    assert(tautology == "ウナギはウナギだ。")
    assert(tautology.distinct == "ウナギはだ。")

    val list: Seq[String] = Seq[String]("安", "衣", "安", "安", "宇", "以", "於", "𠮷")
    assert(list.distinct == Seq[String]("安", "衣", "宇", "以", "於", "𠮷"))
  }
```
<h4>2.5.8　intersect</h4>
```scala
  @Test
  def testIntersect(): Unit = {
    val str1: String = "$ウ$ナ$ギ$は"
    val str2: String = "ウ#ナ#ギ#だ#。#"
    assert(str1.intersect(str2) == "ウナギ")

    val list1: Seq[String] = Seq[String]("安", "衣", "安", "安", "宇", "以", "於", "𠮷")
    val list2: Seq[String] = Seq[String]("衣", "う", "お", "𠮷", "安", "安", "え", "あ")
    println(list1.intersect(list2) == Seq[String]("安", "衣", "安", "𠮷"))
  }
```
<h4>2.5.9　combinations</h4>
```scala
  @Test
  def testCombinations(): Unit = {
    val str: String = "以於𠮷"

    val list: Seq[String] = Seq[String]("以", "於", "𠮷")

    val strCombo: ListBuffer[String] = ListBuffer[String]()
    str.combinations(2) foreach {strCombo.+=}
    assert(strCombo == Seq[String]("以於", "以\uD842", "以\uDFB7", "於\uD842", "於\uDFB7", "𠮷"))

    val listCombo: ListBuffer[Seq[String]] = ListBuffer[Seq[String]]()
    list.combinations(2) foreach {listCombo.+=}
    assert(listCombo == Seq[Seq[String]](Seq("以", "於"), Seq("以", "𠮷"), Seq("於", "𠮷")))
  }
```
<h4>2.5.10　permutations</h4>
```scala
  @Test
  def testPermutations(): Unit = {
    val str: String = "以於𠮷"

    val list: Seq[String] = Seq[String]("以", "於", "𠮷")

    val strPerm: ListBuffer[String] = ListBuffer[String]()
    str.permutations foreach {strPerm.+=}
    assert(strPerm == Seq[String](
      "以於𠮷", "以於\uDFB7\uD842", "以\uD842於\uDFB7", "以𠮷於", "以\uDFB7於\uD842", "以\uDFB7\uD842於",
      "於以𠮷", "於以\uDFB7\uD842", "於\uD842以\uDFB7", "於𠮷以", "於\uDFB7以\uD842", "於\uDFB7\uD842以",
      "\uD842以於\uDFB7", "\uD842以\uDFB7於", "\uD842於以\uDFB7", "\uD842於\uDFB7以", "𠮷以於", "𠮷於以",
      "\uDFB7以於\uD842", "\uDFB7以\uD842於", "\uDFB7於以\uD842", "\uDFB7於\uD842以", "\uDFB7\uD842以於", "\uDFB7\uD842於以"))

    val listPerm: ListBuffer[Seq[String]] = ListBuffer[Seq[String]]()
    list.permutations foreach {listPerm.+=}
    assert(listPerm == Seq[Seq[String]](
      Seq("以", "於", "𠮷"), Seq("以", "𠮷", "於"),
      Seq("於", "以", "𠮷"), Seq("於", "𠮷", "以"),
      Seq("𠮷", "以", "於"), Seq("𠮷", "於", "以")))
  }
```
---
<h3>2.6　インデックス</h3>
<h4>2.6.1　正方向に解析して最初に現れたindexを取得</h4>
<h4>2.6.2　逆方向に解析して最初に現れたindexを取得</h4>
<h4>2.6.3　正方向に解析して条件を最初に満たしたindexを取得</h4>
<h4>2.6.4　逆方向に解析して条件を最初に満たしたindexを取得</h4>
<h4>2.6.5　indexが定義されているか</h4>
---
<h3>2.7　イテレータ</h3>

---
<h3>2.8　文字列間の文字に注目した類似度</h3>
<h4>2.8.1　文字列間の文字に注目した比較単位</h4>
文字はCharではなくコードポイントで扱う方が確実です。Charは文字列にサロゲートペアを含む場合には文字を表しませんが、一般的な英語のテキストようにサロゲートペアが入らないことが保証されている場合は文字として扱うこともできます。
<table>
<tr><th>単位</th><th>説明</th></tr>
<tr>
 <td>文字LCS<br>（Longest Common Subsequence）</td>
 <td>二つの文字列を比較して共通する文字の集合。文字列の順序は考慮する。
 <br>例：
 <br>Y1: [A B C D H I K]
 <br>Y2: [A H B K C I D]
 <br>LCS = ABCD
 </td>
</tr>
<tr>
 <td>文字N-gram<br>（Nは１以上の整数）</td>
 <td>連続するN文字
 <br>例：ABCDEFG
 <br>1-gram = A, B, C, D, E, F, G
 <br>2-gram = AB, BC, CD, DE, EF, FG
 <br>3-gram = ABC, BCD, CDE, DEF, EFG
 </td>
</tr>
<tr>
 <td>任意ギャップの<br>文字Skip N-gram<br>（Nは２以上の整数）</td>
 <td>非連続なN文字
 <br>例：ABCDE
 <br>Skip 2-gram = AB, AC, AD, AE, BC, BD, BE, CD, CE, DE
 <br>Skip 3-gram = ABC, ABD, ABE, ACD, ACE, ADE, BCD, BCE, BDE, CDE
 </td>
</tr>
<tr>
 <td>最大ギャップMの<br>文字Skip N-gram<br>（Nは２以上の整数、Mは１以上の整数）</td>
 <td>非連続なN文字
 <br>例：ABCDE
 <br>Skip 2-gram（最大ギャップ1） = AB, AC, BC, BD, CD, CE, DE
 <br>Skip 2-gram（最大ギャップ2） = AB, AC, AD, BC, BD, BE, CD, CE, DE
 <br>Skip 3-gram（最大ギャップ1） = ABC, ABD, ACD, ACE, BCD, BCE, BDE, CDE
 <br>Skip 3-gram（最大ギャップ2） = ABC, ABD, ABE, ACD, ACE, ADE, BCD, BCE, BDE, CDE
 </td>
</tr>
</table>
N-gramの読み方については、<a href="#コラムn-gramの読み方">コラム：N-gramの読み方</a>をご参照ください。
<h4>2.8.2　文字LCS（自作）</h4>
LCSの長さで類似度を計測することができる。
<h4>2.8.3　レーベンシュタイン距離（自作）</h4>
<h4>2.8.4　ベクトル化（自作）</h4>
<table>
<tr><th>ベクトルの種類</th><th>説明</th></tr>
<tr><td>頻度ベクトル</td><td>重複したら頻度としてカウントして作成するベクトル</td></tr>
<tr><td>二値ベクトル</td><td>重複してもカウントしないので、存在するか否かの二値で作成するベクトル</td></tr>
</table>
<h4>2.8.5　ベクトル間の類似度（自作）</h4>
<h4>2.8.6　ベクトル間の距離（自作）</h4>
<h4>2.8.7　ベクトル間の包含度（自作）</h4>
***
<h3>コラム：N-gramの読み方</h3>
N-gramの数字Nはラテン語の接頭辞表現が使用されます。
例えば1-gram、2-gram、3-gramはラテン語のそれぞれuni、bi、triを用いてunigram、bigram、trigramで表現されます。
読み方はラテン語表記に従って、それぞれ、ユニグラム、バイグラム、トライグラムと読みます。
ただし、変則的ですが4-gram以降はラテン語ではなく英語でfour-gram、five-gram、six-gramなどのように表記しそのように読むのが一般的のようです。

N-gram|異表記|接頭辞の由来|読み方（カタカナ）
---|---|---|---
1-gram|unigram|ラテン語|ユニグラム
2-gram|bigram|ラテン語|バイグラム
3-gram|trigram|ラテン語|トライグラム
4-gram|four-gram|英語|フォーグラム
5-gram|five-gram|英語|ファイブグラム
6-gram|six-gram|英語|シックスグラム
7-gram|seven-gram|英語|セブングラム
8-gram|eight-gram|英語|エイトグラム
9-gram|nine-gram|英語|ナイングラム
10-gram|ten-gram|英語|テングラム
11-gram|eleven-gram|英語|イレブングラム
12-gram|twelve-gram|英語|トゥウェルブグラム
13-gram|thirteen-gram|英語|サーティーングラム

実際にはほとんど使われていませんが、4-gram以降でもしラテン語の接頭辞表現を使用した場合はどうなるかを次の表に載せます。

N-gram|異表記|接頭辞の由来|読み方（カタカナ）
---|---|---|---
4-gram|quadgram|ラテン語|クアッドグラム
4-gram|quadrigram|ラテン語|クアッドリグラム
5-gram|quintgram|ラテン語|クイントグラム
6-gram|sexgram|ラテン語|セクスグラム
7-gram|septgram|ラテン語|セプトグラム
8-gram|octgram|ラテン語|オクトグラム
9-gram|nongram|ラテン語|ノングラム
10-gram|decgram|ラテン語|デクグラム
11-gram|undecgram|ラテン語|ウンデクグラム
12-gram|duodecgram|ラテン語|デュオデクグラム
13-gram|tredecgram|ラテン語|トレデクグラム

ちなみに、写真や動画を共有するSNSのInstagramに着想を得て作られたのであろうポルノ画像や動画を共有するSexgramというAndroidアプリがあるようです。さらに、ラテン語ではなくギリシャ語由来の接頭辞表現を使用すると幾何的なポリグラム（星型のポリゴン）：
Pentagram（五芒星、ペンタグラム）、Hexagram（六芒星、ヘクサグラム）、Heptagram（七芒星、ヘプタグラム）、Octagram（八芒星、オクタグラム）、Enneagram（九芒星、エニアグラム）、Decagram（十芒星、デカグラム）、Hendecagram（十一芒星、ヘンデカグラム）、Dodecagram（十二芒星、ドデカグラム）と名前が被ってしまいます。
