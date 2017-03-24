# 1.　正規表現
正規表現（regular expression、regex）とは、文字列の集合を文字列で表現する方法のことで、文字列のパターンマッチを行うために使用します。
正規表現は<a href="https://ja.wikipedia.org/wiki/%E3%83%81%E3%83%A7%E3%83%A0%E3%82%B9%E3%82%AD%E3%83%BC%E9%9A%8E%E5%B1%A4" target="_blank">チョムスキー階層</a>の３型文法（構文木は根が１つの二分木、入れ子構造を持たない文法、正規文法から生成可能、有限オートマトンによって受理可能）です。一般的にプログラミングにおける正規表現は、どんな文字にも一致する特殊文字「ワイルドカード」を加えて組み合わせたものを指します。厳密には、正規表現とワイルドカードはプログラミング言語設計上別の機構ですが、パターンマッチを行う上で両方をシームレスに使えるので、ここでの正規表現は便宜上ワイルドカードと組み合わせたものを指すことにします。<br>
<br>
正規表現の例としてUCS-2（2-byte Universal Character Set）、UTF-16（ビッグエンディアン）、UTF-16（リトルエンディアン）の正規表現を次に示します。<br><br>
例：UCS-2を表す正規表現<strong>（ScalaのChar / Javaのcharに相当）</strong>

```java
[\\x00-\\xFF][\\x00-\\xFF]
```

例：UTF-16（ビッグエンディアン）を表す正規表現

BOM：

```java
\\xFE\\xFF
```

文字：<strong>（Scala/Javaの文字）</strong>

```java
(?:[\\x00-\\xD7\\xE0-\\xFF][\\x00-\\xFF]|[\\xD8-\\xDB][\\x00-\\xFF][\\xDC-\\xDF][\\x00-\\xFF])
```
UTF-16（ビッグエンディアン）の文字の構造がわかりやすいように正規表現に改行とインデントとコメントを加えた図：
```java
(?:
               [\\x00-\\xD7\\xE0-\\xFF][\\x00-\\xFF]| // UCS-2
[\\xD8-\\xDB][\\x00-\\xFF][\\xDC-\\xDF][\\x00-\\xFF]  // UTF-16代理領域（サロゲートペア）
//（左の2オクテットがhigh surrogate、右の2オクテットがlow surrogate）
)
```

例：UTF-16（リトルエンディアン）を表す正規表現

BOM：

```java
\\xFF\\xFE
```

文字：

```java
(?:[\\x00-\\xFF][\\x00-\\xD7\\xE0-\\xFF]|[\\x00-\\xFF][\\xD8-\\xDB][\\x00-\\xFF][\\xDC-\\xDF])
```
UTF-16（リトルエンディアン）の文字の構造がわかりやすいように正規表現に改行とインデントとコメントを加えた図：

```java
(?:
               [\\x00-\\xFF][\\x00-\\xD7\\xE0-\\xFF]| // UCS-2
[\\x00-\\xFF][\\xD8-\\xDB][\\x00-\\xFF][\\xDC-\\xDF]  // UTF-16代理領域（サロゲートペア）
//（右の2オクテットがlow surrogate、左の2オクテットがhigh surrogate）
)
```
Scala / Javaにおける正規表現の定義は<a href="http://docs.oracle.com/javase/jp/8/docs/api/java/util/regex/Pattern.html" target="_blank">JavaのJavadocのPatternクラス</a>に書かれています。
詳細な定義はPatternクラスの説明を読んでください。
ここでは、処理の目的を、一致、分割、抽出、置換に分けてそれぞれの処理で正規表現を使い方を説明します。

正規表現は、Stringクラス、Patternクラス、Regexクラスのメソッドで使用できます。
Stringクラスで正規表現を引数とするメソッドはかなりユーティリティが非常に高く適応範囲がかなり限定的なもの（matches、split）です。
正規表現で可能な全ての処理を行うにはPatternクラスかRegexクラスを使用する必要があります。
PatternクラスはJavaのAPIで、RegexクラスはScalaのAPIです。
Regexクラスを使用するとmatch-case文でパターンマッチの分岐を書いたり、抽出したグループの変数名を指定することができ、Patternクラスよりも直感的に実装できます。

なお、人間が使用する自然言語はチョムスキー階層での何型文法なのか興味がある人は<a href="#コラム自然言語はチョムスキー階層での何型文法">コラム：自然言語はチョムスキー階層での何型文法？</a>を参照してください。
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
表層文字列の完全一致は==演算子かJava由来のequalsメソッドを使用します。
Javaでは、==演算子を使用すると参照の一致を見てしまい、文字列としては等値であってもfalseを返してしまう可能性があります。そのため、文字列としての等値を正しく返すためにequalsメソッドを使用しました。
letter caseを無視して（例えば、全てlower caseに揃えて）から完全一致を見る場合はequalsIgnoreCaseメソッドを使用します。
==演算子やequalsメソッドの代替にcompareメソッド、compareToメソッドや、equalsIgnoreCaseメソッドの代わりにcompareToIgnoreCaseメソッドを使用することができますが、==演算子やequalsメソッドはnullを比較する場合でもBooleanを返しますが、compare系メソッドはnullに対してjava.lang.NullPointerExceptionを返す点が違います。
Scalaでは、==演算子を使用すると文字列としての等値を見ることができますが、Javaの==演算子のように参照の一致を知りたい場合は、一致を見る場合はeq演算子、不一致を見る場合はne演算子を使用します。

```scala
  private val unagiCopula: String = "僕はウナギ"
  
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
```
***
<h4>1.1.2　完全一致（正規表現）</h4>
パターンマッチの完全一致を正規表現を用いて見る場合は、Stringクラスのmatchesメソッド、Patternクラスのmatchesメソッド、Matcherクラスのmatchesメソッド、Regexクラスを用いる方法があります。Stringクラスのmatchesメソッドは処理速度が遅いです。PatternクラスのmatchesメソッドとRegexクラスではPatternクラスを使用する方が処理速度が速いです。何度も同じパターンで一致を見る場合は、Patternクラスは一度compileメソッドでコンパイルしてPatternのインスタンスを生成しておいて、それを何度も使用する方が高速ですし、Regexクラスについても、一度StringクラスのrメソッドでRegexインスタンスを生成しておいて、それを何度も使用する方が高速です。この場合でもPatternインスタンスを使用する方がRegexインスタンスを使用するより高速です。コンパイルされたPatternインスタンスはmatcherメソッドで対象の文字列を与え、Matcherクラスのインスタンスを生成し、Matcherクラスのmatchesメソッドを使用することで完全一致したかを確認できます。

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
```
regionMatchesメソッドにより、一致範囲を指定して、正規表現による完全一致を調べることができます。

```scala
  @Test
  def testExactMatch5(): Unit = {
    assert(unagiCopula.regionMatches(2, "ウナギだ", 0, 3))

    //第一引数はignoreCase: Boolean
    assert(unagiCopula.regionMatches(true, 2, "ウナギだ", 0, 3))
    assert(unagiCopula.regionMatches(false, 2, "ウナギだ", 0, 3))
  }
```
***
<h4>1.1.3　部分一致（表層文字列）</h4>
表層文字列の部分一致を見るためには、Java由来のcontainsメソッドとScala由来のcontainsSliceメソッドがあります。
containsメソッドはStringクラスのindexOfメソッドを使用して実装されています。indexOfメソッドは先頭から順番に一つずつ見ていき一致したらその位置インデックスを返すメソッドです（この方法は、「Brute-force search」もしくは「力まかせ探索」と言います）。もし見つからなかった場合はindexOfメソッドから-1が返ってきますので、返り値が-1ではなければcontainsメソッドはtrueを返し、-1の場合はfalseを返します。
containsSliceメソッドはSeqLikeクラスのindexOfSliceメソッドを使用して、containsメソッドと同様にindexOfSliceメソッドの返り値が-1でなければtrue、-1の場合はfalseを返します。indexOfSliceメソッドは<a href="https://ja.wikipedia.org/wiki/%E3%82%AF%E3%83%8C%E3%83%BC%E3%82%B9%E2%80%93%E3%83%A2%E3%83%AA%E3%82%B9%E2%80%93%E3%83%97%E3%83%A9%E3%83%83%E3%83%88%E6%B3%95" target="_blank">クヌース–モリス–プラット法</a>（以下、KMP法）を用いて実装されています。
処理速度は基本的にはcontainsメソッドの方がcontainsSliceメソッドより速いです。
containsSliceはKMP法で実装されているので、その分のオーバーヘッドが乗ります。しかし、もしも一致ではないが似ている文字列が多く含まれているような場合（例えば、DNA中に特定の遺伝子配列が含まれているか調べる場合）にはKMP法で実装されているcontainsSliceメソッドの方がcontainsメソッドより効率的に処理を行います。KMP法のような文字列探索アルゴリズムについては<a href="#コラム文字列探索アルゴリズム">コラム：文字列探索アルゴリズム</a>をご参照ください。

```scala
  private val unagiCopula: String = "僕はウナギ"
  
  @Test
  def testContains(): Unit = {
    assert(unagiCopula.contains("ウナギ"))
    assert(unagiCopula.containsSlice("ウナギ"))
  }
```
***
<h4>1.1.4　部分一致（正規表現）</h4>
一回だけ部分一致をみたい場合は、Patternクラスのfindメソッドを使用する方法とRegexクラスのfindFirstInメソッドを使用する方法とRegexクラスのfindFirstMatchInメソッドを使用する方法があります。全ての部分一致を見たい場合は、Patternクラスのfindメソッドとnextメソッドを使用する方法とRegexクラスのfindAllInメソッドを使用する方法とRegexクラスのfindAllMatchInメソッドを使用する方法があります。RegexクラスのfindFirstInメソッドとfindAllInメソッドはそれぞれOption[String]とMatchIteratorを返します。RegexクラスのfindFirstMatchInメソッドやfindAllMatchInメソッドはOption[Match]とIterator[Match]を返します。RegexクラスとMatchクラスの関係はJava由来のPatternクラスに対するMatcherクラスの関係と似ており、Matchクラスの多くのメソッドはMatcherクラスにも同様に存在します。

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
表層文字列の前方一致を見る場合はStringクラスのstartsWithメソッドを使用します。

```scala
  @Test
  def testStartsWith(): Unit = {
    assert(tautology.startsWith("ウナギ"))
  }
```
***
<h4>1.1.6　前方一致（正規表現）</h4>
パターンマッチによる前方一致を見る場合は、PatternクラスとMatcherクラスを用いて、MatcherクラスのlookingAtメソッドを使用することができます。部分一致の方法を用いて、正規表現で書いたパターンに対して前方一致を示す「^」を先頭に加える方法もあります。

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
表層文字列の後方一致を見る場合はStringクラスのendsWithメソッドを使用します。

```scala
  @Test
  def testEndsWith(): Unit = {
    assert(tautology.endsWith("ギだ。"))
  }
```
***
<h4>1.1.8　後方一致（正規表現）</h4>
正規表現で後方一致を見る場合は、専用のメソッドがありません。
例えば、一致を見たい文字列を逆順にして、MatcherクラスのlookingAtメソッドで一致が見れるようなパターンを用意しておき、後方一致を前方一致の方法で見る方法もあります。部分一致の方法を用いて、正規表現で書いたパターンに対して後方一致を示す「$」を末尾に加える方法もあります。

```scala
  @Test
  def testBackwardMatch(): Unit = {
    val pattern: Pattern = Pattern.compile("。だギ")
    val matcher: Matcher = pattern.matcher(tautology.reverse)

    assert(matcher.lookingAt())
  }
```
***
<h4>1.1.9　最長一致・最短一致</h4>
<ul>
<li>最長一致</li>
<li>最短一致</li>
</ul>

```scala
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
```
***
<h3>1.2　分割</h3>
文字列を分割するためには分割位置の与える必要があります。分割位置を与える方法として、文字列中の区切り文字の存在を利用する方法、区切り文字の特殊な例として行末文字を用いる方法、インデックスで直接指定する方法、文字列中のCharを前方から見てある条件を満たさなくなった位置を使用する方法があります。それぞれについて下記で説明します。
<h4>1.2.1　区切り文字による分割</h4>
区切り文字（デリミタ、delimiter）でトークン（token）に分割（split）します。
よくCSV、TSV、SSVファイルや統語解析器の出力結果をパースするときに使用します。<a href="https://github.com/ynupc/scalastringcourseday5/blob/master/doc/mutability.md" target="_blank">Day 5</a>で紹介したStringJoinerやString.joinメソッドでトークンをデリミタで結合するのとちょうど逆の処理になります。
この分割処理を行うためのクラス<a href="https://docs.oracle.com/javase/jp/8/docs/api/java/util/StringTokenizer.html" target="_blank">StringTokenizer</a>はJava 8でも動作しますが、Java 5以降互換性を保つためのレガシークラスとなっており、使用が推奨されておりませんのでご注意ください。
処理速度の面では、非推奨のStringTokenizerを使用した場合が最速で、次にStringクラスのsplitメソッドが高速です。Patternクラスを用いる場合は、何度もsplitメソッドを呼び出すとき、最初にcompileメソッドで区切り文字の正規表現をコンパイルしてPatternインスタンスを生成し、そのインスタンスを使い回す方がインスタンス生成のためのオーバーヘッドがなく、処理速度とさらにメモリ効率の面でも好ましいです。

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
```
<h4>1.2.2　行末文字による分割</h4>
linesメソッドやlinesWithSeparatorsメソッドを用いて行末文字で分割します。
行末文字とは、改行文字LF（Line Feed）のU+000Aと改ページFF（Form Feed）のU+000Cを指します。
linesメソッドは分割後に分割された文字列から行末文字を削除しますが、linesWithSeparatorsメソッドは分割後も行末文字が残ります。

```scala
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
```
<h4>1.2.3　分割位置をインデックスによる指定した分割</h4>
StringクラスのsplitAtメソッドを用いて分割位置を指定して分割します。

```scala
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
```
<h4>1.2.4　条件に従わなくなった位置による分割</h4>
Stringクラスからspanメソッドを使用して条件に従わなくなった位置で分割します。

```scala
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
```
***
<h3>1.3　置換</h3>
文字列がパターンにマッチしたらマッチした箇所を他の文字列に置き換えます。置き換える文字列を空文字列にすることでマッチした箇所を削除することも可能です。

<h4>1.3.1　表層文字列の一致による置換</h4>
Charの置換はStringクラスのreplaceメソッドで行います。
Stringの置換はJava由来のStringクラスのreplaceメソッドで行うか、Scala由来のreplaceAllLiterallyメソッドで行います。
replaceメソッドもreplaceAllLiterallyメソッドも内部では最終的にMatcherクラスのreplaceAllに投げますが、途中過程が異なるためreplaceメソッドの方がreplaceAllLiterallyメソッドよりも高速です。
replaceメソッドやreplaceAllメソッドの引数をreplaceAllメソッドで読み込めるように修正しますが、置換文字列はどちらもMatcherクラスのquoteReplacementメソッドを用いて置換文字列中の"\"や"$"の直前に"\"を挿入してエスケープシーケンスにしています。被置換文字列はreplaceメソッドの場合PatternクラスのcompileメソッドでPatternインスタンス生成するときにPattern.LITERALフラグを渡して生成し、そのPatternインスタンスからmatcherメソッドでMatcherインスタンスを生成します。そして、そのMatcherインスタンスのreplaceAllメソッドを使用して置換します。一方で、replaceAllLiterallyメソッドでは、Pattern.LITERALフラグを使用せずに、Patternクラスのquoteメソッドを使用して、正規表現の先頭と末尾にそれぞれ"\\Q"と"\\E"を付け加えて、エスケープシーケンスの"\"を二重に書かずに済むような正規表現をStringとして取得します。StringクラスのreplaceAllメソッドにそれらを投げることでStringクラスのreplaceAllメソッドの内部でMatcherクラスのreplaceAllメソッドが呼び出される仕組みです。
**Pattern.LITERALフラグにより直接Patternインスタンスを生成するのか、正規表現の先頭と末尾にそれぞれ"\\Q"と"\\E"を付け加えてPatternインスタンスを生成するのかの処理の差分によりreplaceメソッドの方がreplaceAllLiterallyメソッドより高速です。**
処理速度とは別の観点として**プログラムの可読性を高めるためにreplaceAllLiterallyメソッドを使用するという考え方もあります。**

```scala
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
```

<h4>1.3.2　正規表現の一致による置換</h4>
正規表現の一致で、最初に一致した箇所で置換する場合はStringクラスかMatcherクラスのreplaceFirstメソッド、一致した全ての箇所で置換する場合はStringクラスかMatcherクラスのreplaceAllメソッドを使用します。Stringクラスのメソッドは内部的にはMatcherクラスのメソッドを呼び出しているため、一度使用する場合は処理速度に違いはありません。複数回同じ置換処理を行う場合は、Patternインスタンスを生成しそれを使い回してMatcherインスタンスを生成すると処理を高速化できます。

```scala
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
```
<h4>1.3.3　範囲指定による置換</h4>
位置や範囲をインデックスで指定して置換をします。
一文字のみを置換したい場合はupdatedメソッドを使用します。
文字を複数含む範囲で置換したい場合はpatchメソッドを使用します。

```scala
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
```
<h4>1.3.4　複数のCharの同時置換</h4>
collectFirstメソッドやcollectメソッドを使用することで、一致した文字を別の文字に変換します。
replaceメソッドによる文字の置換を複数回用いる場合は、replaceメソッドを呼び出す順序により結果が異なる可能性がある点と、replaceメソッドを使うたびにStringが生成されて非効率である点で異なります。

```scala
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
```
***
<h3>1.4　抽出</h3>
文字列からパターンマッチにより部分的な文字列を抽出するためにグループが使われます。<br>
正規表現内を()で囲むとグループが作れます。左から右に左丸括弧を数えることでグループ番号が振られます。例えば、ウ((ナ)(ギ))の場合、次のように番号付けされます。

グループ番号|部分シーケンス|備考
---|---|---
0|ウ((ナ)(ギ))|0番には一致箇所の全体が入ります。
1|((ナ)(ギ))|
2|(ナ)|
3|(ギ)|

```scala
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
```

***
<h3>コラム：自然言語はチョムスキー階層での何型文法？</h3>
ノーム・チョムスキー
言語獲得
生成文法

複雑性
<h4>（１）Complexity Zoo</h4>
https://www.math.ucdavis.edu/~greg/zoology/diagram.xml

<h4>（２）Chomsky hierarchy</h4>
ノーム・チョムスキー
Type-2（文脈自由文法）では不十分ではないか
Type-1で不十分という事例は出ていない
自然言語はType-1（１型文法）とType-2（２型文法）の間かも？
生成文法、確率的文脈自由文法、木接合文法、主辞駆動句構造文法が生まれた。
言語やタスクに内在する複雑性を説明するための感覚的な指標としても利用できる。
<table>
<thead>
<caption>Automata theory: formal languages and formal grammars</caption>
<tr>
<th>Chomsky hierarchy</th>
<th>Grammars</th>
<th>Languages</th>
<th>Abstract machines</th>
<th>Complexity Zoo</th>
</tr>
</thead>
<tbody>
<tr>
<td>Type-0</td>
<td>Unrestricted</td>
<td>Recursively enumerable</td>
<td>Turing machine</td>
<td>ALL</td>
</tr>
<tr>
<td>—</td>
<td>(no common name)</td>
<td>Decidable</td>
<td>Decider</td>
<td>R</td>
</tr>
<tr>
<td>Type-1</td>
<td>Context-sensitive</td>
<td>Context-sensitive</td>
<td>Linear-bounded</td>
<td>CSL</td>
</tr>
<tr>
<td>—</td>
<td>Positive range concatenation</td>
<td>Positive range concatenation*</td>
<td>PTIME Turing Machine</td>
<td></td>
</tr>
<tr>
<td>—</td>
<td>Indexed</td>
<td>Indexed*</td>
<td>Nested stack</td>
<td></td>
</tr>
<tr>
<td>—</td>
<td>—</td>
<td>—</td>
<td>Thread automaton</td>
<td></td>
</tr>
<tr>
<td>—</td>
<td>Linear context-free rewriting systems</td>
<td>Linear context-free rewriting language</td>
<td>—</td>
<td></td>
</tr>
<tr>
<td>—</td>
<td>Tree-adjoining</td>
<td>Tree-adjoining</td>
<td>Embedded pushdown</td>
<td></td>
</tr>
<tr>
<td>Type-2</td>
<td>Context-free</td>
<td>Context-free</td>
<td>Nondeterministic pushdown</td>
<td>CFL</td>
</tr>
<tr>
<td>—</td>
<td>Deterministic context-free</td>
<td>Deterministic context-free</td>
<td>Deterministic pushdown</td>
<td>DCFL</td>
</tr>
<tr>
<td>—</td>
<td>Visibly pushdown</td>
<td>Visibly pushdown</td>
<td>Visibly pushdown</td>
<td></td>
</tr>
<tr>
<td>Type-3</td>
<td>Regular</td>
<td>Regular</td>
<td>Finite</td>
<td>REG</td>
</tr>
<tr>
<td>—</td>
<td>—</td>
<td>Star-free</td>
<td>Counter-free (with aperiodic finite monoid)</td>
<td></td>
</tr>
<tr>
<td>—</td>
<td>Non-recursive</td>
<td>Finite</td>
<td>Acyclic finite</td>
<td></td>
</tr>
</tbody>
<tfoot>
<tr>
<td colspan="5">Each category of languages, except those marked by a *, is a proper subset of the category directly above it. Any language in each category is generated by a grammar and by an automaton in the category in the same line.</td>
</tr>
</tfoot>
</table>
ノーム・チョムスキーの言語獲得に関する理論の変遷
***
<h3>コラム：文字列探索アルゴリズム</h3>
<ul>
<li>Aho–Corasick algorithm</li>
<li>Commentz-Walter algorithm</li>
<li>LOUDS</li>
<li>Suffix array algorithm</li>
<li>Suffix tree algorithm</li>
</ul>
<a href="http://www-igm.univ-mlv.fr/~lecroq/string/" target="_blank">EXACT STRING MATCHING ALGORITHMS Animation in Java</a>に次のアルゴリズムの説明があります。
<ul>
<li>Brute Force algorithm</li>
<li>Deterministic Finite Automaton algorithm</li>
<li>Karp-Rabin algorithm</li>
<li>Shift Or algorithm</li>
<li>Morris-Pratt algorithm</li>
<li>Knuth-Morris-Pratt algorithm</li>
<li>Simon algorithm</li>
<li>Colussi algorithm</li>
<li>Galil-Giancarlo algorithm</li>
<li>Apostolico-Crochemore algorithm</li>
<li>Not So Naive algorithm</li>
<li>Boyer-Moore algorithm</li>
<li>Turbo BM algorithm</li>
<li>Apostolico-Giancarlo algorithm</li>
<li>Reverse Colussi algorithm</li>
<li>Horspool algorithm</li>
<li>Quick Search algorithm</li>
<li>Tuned Boyer-Moore algorithm</li>
<li>Zhu-Takaoka algorithm</li>
<li>Berry-Ravindran algorithm</li>
<li>Smith algorithm</li>
<li>Raita algorithm</li>
<li>Reverse Factor algorithm</li>
<li>Turbo Reverse Factor algorithm</li>
<li>Forward Dawg Matching algorithm</li>
<li>Backward Nondeterministic Dawg Matching algorithm</li>
<li>Backward Oracle Matching algorithm</li>
<li>Galil-Seiferas algorithm</li>
<li>Two Way algorithm</li>
<li>String Matching on Ordered Alphabets algorithm</li>
<li>Optimal Mismatch algorithm</li>
<li>Maximal Shift algorithm</li>
<li>Skip Search algorithm</li>
<li>KMP Skip Search algorithm</li>
<li>Alpha Skip Search algorithm</li>
</ul>
