# 1.　正規表現
正規表現（regular expression、regex）とは、文字列の集合を文字列で表現する方法のことで、文字列のパターンマッチを行うために使用します。
正規表現は<a href="https://ja.wikipedia.org/wiki/%E3%83%81%E3%83%A7%E3%83%A0%E3%82%B9%E3%82%AD%E3%83%BC%E9%9A%8E%E5%B1%A4" target="_blank">チョムスキー階層</a>の３型文法（構文木は根が１つの二分木、入れ子構造を持たない文法、正規文法から生成可能、有限オートマトンによって受理可能）です。一般的にプログラミングにおける正規表現は、どんな文字にも一致する特殊文字「ワイルドカード」を加えて組み合わせたものを指します。厳密には、正規表現とワイルドカードはプログラミング言語設計上別の機構ですが、パターンマッチを行う上で両方をシームレスに使えるので、ここでの正規表現は便宜上ワイルドカードと組み合わせたものを指すことにします。<br>
<br>
正規表現の例としてUCS-2（2-byte Universal Character Set）、UTF-16（ビッグエンディアン）、UTF-16（リトルエンディアン）の正規表現を次に示します。<br><br>
例：UCS-2を表す正規表現<strong>（ScalaのChar / Javaのcharに相当）</strong>
```
[\\x00-\\xFF][\\x00-\\xFF]
```

例：UTF-16（ビッグエンディアン）を表す正規表現

BOM：
```
\\xFE\\xFF
```

文字：<strong>（Scala/Javaの文字）</strong>

```
([\\x00-\\xD7\\xE0-\\xFF][\\x00-\\xFF]|[\\xD8-\\xDB][\\x00-\\xFF][\\xDC-\\xDF][\\x00-\\xFF])
```
UTF-16（ビッグエンディアン）の文字の構造がわかりやすいように正規表現に改行とインデントとコメントを加えた図：
```
(
               [\\x00-\\xD7\\xE0-\\xFF][\\x00-\\xFF]|//UCS-2
[\\xD8-\\xDB][\\x00-\\xFF][\\xDC-\\xDF][\\x00-\\xFF] //UTF-16代理領域（サロゲートペア）
//（左の2オクテットがhigh surrogate、右の2オクテットがlow surrogate）
)
```

例：UTF-16（リトルエンディアン）を表す正規表現

BOM：
```
\\xFF\\xFE
```

文字：
```
([\\x00-\\xFF][\\x00-\\xD7\\xE0-\\xFF]|[\\x00-\\xFF][\\xD8-\\xDB][\\x00-\\xFF][\\xDC-\\xDF])
```
UTF-16（リトルエンディアン）の文字の構造がわかりやすいように正規表現に改行とインデントとコメントを加えた図：
```
(
               [\\x00-\\xFF][\\x00-\\xD7\\xE0-\\xFF]|//UCS-2
[\\x00-\\xFF][\\xD8-\\xDB][\\x00-\\xFF][\\xDC-\\xDF] //UTF-16代理領域（サロゲートペア）
//（右の2オクテットがlow surrogate、左の2オクテットがhigh surrogate）
)
```
Scala / Javaにおける正規表現の定義は<a href="http://docs.oracle.com/javase/jp/8/docs/api/java/util/regex/Pattern.html" target="_blank">JavaのJavadocのPatternクラス</a>に書かれています。
詳細な定義はPatternクラスの説明を読んでください。ここでは、処理の目的を、一致、分割、抽出、置換に分けてそれぞれの処理で正規表現を使い方を説明します。
人間が使用する自然言語はチョムスキー階層での何型文法なのか興味がある人は<a href="#コラム自然言語はチョムスキー階層での何型文法">コラム：自然言語はチョムスキー階層での何型文法？</a>を参照してください。
<br>

***
<h3>1.1　一致</h3>
<ul>
  <li>完全一致（exact match）：ABCDはABCDに完全一致</li>
  <li>部分一致（broad match / partial match）：BCはABCDに部分一致、下記の前方一致・後方一致は部分一致の特殊例、一般的に完全一致は部分一致に含めませんが特殊例として解釈することも可能です。
　  <ul>
　    <li>前方一致（forward match）：ABはABCDに前方一致</li>
　    <li>後方一致（backward match）：CDはABCDに後方一致</li>
　  </ul>
　</li>
</ul>
<table>
<tr>
<th>一致の種類</th>
<th>表層文字列一致のメソッド名</th>
<th>正規表現によるパターンマッチのメソッド名</th>
</tr>
<tr>
<td>完全一致</td>
<td>equals, ==</td>
<td>matches</td>
</tr>
<tr>
<td>部分一致</td>
<td>contains</td>
<td>find</td>
</tr>
<tr>
<td>前方一致</td>
<td>startsWith</td>
<td>lookingAt</td>
</tr>
<tr>
<td>後方一致</td>
<td>endsWith</td>
<td>&nbsp;</td>
</tr>
</table>
<table>
<tr>
<th>一致の種類</th>
<th>次の正規表現を使うと完全一致のmatchesメソッドなどで実装可能</th>
</tr>
<tr>
<td>完全一致</td>
<td>[正規表現]</td>
</tr>
<tr>
<td>部分一致</td>
<td>.*[正規表現].*</td>
</tr>
<tr>
<td>前方一致</td>
<td>[正規表現].*</td>
</tr>
<tr>
<td>後方一致</td>
<td>.*[正規表現]</td>
</tr>
</table>
<table>
<tr>
<th>一致の種類</th>
<th>次の正規表現を使うと部分一致のfindメソッドなどで実装可能</th>
</tr>
<tr>
<td>完全一致</td>
<td>^[正規表現]$</td>
</tr>
<tr>
<td>部分一致</td>
<td>[正規表現]</td>
</tr>
<tr>
<td>前方一致</td>
<td>^[正規表現]</td>
</tr>
<tr>
<td>後方一致</td>
<td>[正規表現]$</td>
</tr>
</table>
<table>
<tr>
<th>一致の種類</th>
<th>次の正規表現を使うと前方一致のlookingAtメソッドで実装可能</th>
</tr>
<tr>
<td>完全一致</td>
<td>[正規表現]$</td>
</tr>
<tr>
<td>部分一致</td>
<td>.*[正規表現]</td>
</tr>
<tr>
<td>前方一致</td>
<td>[正規表現]</td>
</tr>
<tr>
<td>後方一致</td>
<td>&nbsp;</td>
</tr>
</table>
***
<h4>1.1.1　完全一致（表層文字列）</h4>
```scala
  private val unagiCopula: String = "僕はウナギ"
  
  @Test
  def testStringEqualExpression(): Unit = {
    assert(unagiCopula == "僕はウナギ")
    assert(unagiCopula.equals("僕はウナギ"))

    assert(unagiCopula != "僕はウサギ")
    assert(!unagiCopula.equals("僕はウサギ"))
  }

  @Test
  def testStringEqualExpressionIngnoreCase(): Unit = {
    assert(!"I am Unagi.".equals("i am unagi."))
    assert("I am Unagi.".equalsIgnoreCase("i am unagi."))
  }

  @Test
  def testStringEqualExpression2(): Unit = {
    assert(unagiCopula eq "僕はウナギ")
    assert(unagiCopula ne "僕はウサギ")
  }
```
***
<h4>1.1.2　完全一致（正規表現）</h4>
```scala
  private val unagiCopula: String = "僕はウナギ"
  
  @Test
  def testExactMatch1(): Unit = {
    assert(unagiCopula.matches("僕はウナギ"))
  }

  @Test
  def testExactMatch2(): Unit = {
    assert(Pattern.matches("僕はウナギ", unagiCopula))
  }

  @Test
  def testExactMatch4(): Unit = {
    val pattern: Pattern = Pattern.compile("僕はウナギ")
    val matcher: Matcher = pattern.matcher(unagiCopula)

    assert(matcher.matches())
  }

  @Test
  def testExactMatch5(): Unit = {
    val regex: Regex = """僕はウナギ""".r
    unagiCopula match {
      case regex() =>
        assert(true)
      case otherwise =>
        assert(false)
    }
  }
```
***
<h4>1.1.3　部分一致（表層文字列）</h4>
```scala
  private val unagiCopula: String = "僕はウナギ"
  
  @Test
  def testContains(): Unit = {
    assert(unagiCopula.contains("ウナギ"))
  }
```
***
<h4>1.1.4　部分一致（正規表現）</h4>

```scala
  private val tautology: String = "ウナギはウナギだ。"

  @Test
  def testBroadMatch1(): Unit = {
    val pattern: Pattern = Pattern.compile("ウナギ")
    val matcher: Matcher = pattern.matcher(tautology)

    assert(matcher.find())
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
```
***
<h4>1.1.5　前方一致（表層文字列）</h4>
```scala
  @Test
  def testStartsWith(): Unit = {
    assert(tautology.startsWith("ウナギ"))
  }
```
***
<h4>1.1.6　前方一致（正規表現）</h4>

```scala
  @Test
  def testForwardMatch(): Unit = {
    val pattern: Pattern = Pattern.compile("ウナギ")
    val matcher: Matcher = pattern.matcher(tautology)

    assert(matcher.lookingAt())
  }
```
***
<h4>1.1.7　後方一致（表層文字列）</h4>
```scala
  @Test
  def testEndsWith(): Unit = {
    assert(tautology.endsWith("ギだ。"))
  }
```
***
<h4>1.1.8　後方一致（正規表現）</h4>

```scala
  @Test
  def testBackwardMatch(): Unit = {
    val pattern: Pattern = Pattern.compile("ギだ。".reverse)
    val matcher: Matcher = pattern.matcher(tautology.reverse)

    assert(matcher.lookingAt())
  }
```
***
<h3>1.2　分割</h3>
区切り文字（デリミタ、delimiter）でトークン（token）に分割（split）します。
よくCSV、TSV、SSVファイルや統語解析器の出力結果をパースするときに使用します。<a href="https://github.com/ynupc/scalastringcourseday5/blob/master/doc/mutability.md" target="_blank">Day 5</a>で紹介したStringJoinerやString.joinメソッドでトークンをデリミタで結合するのとちょうど逆の処理になります。
この分割処理を行うためのクラス<a href="https://docs.oracle.com/javase/jp/8/docs/api/java/util/StringTokenizer.html" target="_blank">StringTokenizer</a>はJava 8でも動作しますが、Java 5以降互換性を保つためのレガシークラスとなっており、使用が推奨されておりませんのでご注意ください。
```scala
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
```
***
<h3>1.3　抽出</h3>
文字列からパターンマッチにより部分的な文字列を抽出するためにグループが使われます。<br>
正規表現内を()で囲むとグループが作れます。左から右に左丸括弧を数えることでグループ番号が振られます。例えば、((A)(B(C)))Dの場合、次のように番号付けされます。

グループ番号|部分シーケンス|備考
---|---|---
0|((A)(B(C)))D|0番には入力シーケンス自体が入ります。
1|((A)(B(C)))|
2|(A)|
3|(B(C))|
4|(C)|
***
<h3>1.4　置換</h3>
文字列がパターンにマッチしたらマッチした箇所を他の文字列に置き換えます。置き換える文字列を空文字列にすることでマッチした箇所を削除することも可能です。
***
<h3>コラム：自然言語はチョムスキー階層での何型文法？</h3>
自然言語は１型文法と２型文法の間かも？
