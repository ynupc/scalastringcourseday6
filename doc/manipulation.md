# 2.　文字列操作
<h3>2.1　比較</h3>
文字列間の等値、表層文字列による一致や正規表現によるパターンマッチについては前章で説明しました。
ここでは、文字をアルファベット・あいうえお順（厳密にはChar順）に並べる方法を説明します。<br>
&lt;演算子（&gt;演算子）は文字・文字列に対しては、Char順に基づいて比較されます。
'A'と'B'は'A'より'B'の方がChar順で後ろにあるため、'A' &lt; 'B'はtrueで、'A' &lt; 'A'や'B' &lt; 'A'はfalseです。
"AA" &lt; "AB"はtrueです。"B"と"BA"のように先頭が一致するが長さが違う場合は短い方が前、長い方が後ろになります。つまり、"B" &lt; "BA"はtrueです。
&lt;=演算子（&gt;=演算子）は&lt;演算子（&gt;演算子）に一致を加えた機能です。<br>
compareToメソッドとcompareメソッドは名前が違いますが、compareメソッドはcompareToメソッドを呼び出すだけなので機能としては同じです。compareToメソッドは２つの文字や文字列を比較した結果を、0より小さいか、0か、もしくは0より大きいかの３値によって、Char順に基づいて比較することができます。
例えば、"A".compareTo("B")の場合は、compareToメソッドを理解する上で便宜上compareToを「-」に置換すると、"A" - "B"となりますが、"A"より"B"の方がChar順で後ろにあるため"A" - "B" &lt; 0です。この「-」をcompareToに元に戻すと、"A".compareTo("B") &lt; 0になりますが、これはtrueです。
compareToIgnoreCaseメソッドは、Stringクラスにあるメソッドで、全ての文字のletter caseを無視した（例えば、lower caseに揃えた）状態でcompareToメソッドを使ったのと同等の機能です。
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
```
OpenJDK 8u40-b25の<a href="http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/java/lang/String.java#String.equalsIgnoreCase%28java.lang.String%29" target="_blank">equalsIgnoreCaseメソッド</a><br>
equalsIgnoreCaseメソッドはletter caseを無視した等値を見るために次の順で判定します。
<ol>
<li>Stringと引数のStringの参照が一致したらtrue</li>
<li>引数のStringがnullだったらfalse</li>
<li>Stringと引数のStringの長さが一致しなかったらfalse</li>
<li>Stringと引数のStringの両方に対し（この時Stringと引数のStringの両方の長さは一致している）、先頭のCharから順に観察し全てのCharに対し次の条件を全て満たした場合はtrue、１回でも違反した場合はfalse
<ol>
<li>Stringと引数のStringの同じインデックスのCharが一致する</li>
<li>または、Stringと引数のStringの同じインデックスのCharをupper caseに変換したCharが一致する（letter caseを無視するため）</li>
<li>または、Stringと引数のStringの同じインデックスのCharをupper caseに変換したCharをlower caseに変換したCharが一致する（upper caseに変換しただけでは<a href="https://ja.wikipedia.org/wiki/%E3%82%B0%E3%83%AB%E3%82%B8%E3%82%A2%E6%96%87%E5%AD%97" target="_blank">グルジア文字</a>に対してはletter caseを無視できていないため、さらにlower caseに変換することでグルジア文字のletter caseを無視するためだそうですが、私が調べた限りではそのような問題は起こらないように見えます。参照：<a href="#コラムグルジア文字のletter-case">コラム：グルジア文字のletter case</a>。しかし、グルジア文字とは別の文字にはletter caseの変換に非対称性が存在します。参照：<a href="#コラムletter-caseの変換の非対称性">コラム：letter caseの変換の非対称性</a>。）</li>
</ol>
</li>
</ol>

```java
public boolean equalsIgnoreCase(String anotherString) {
  return (this == anotherString) ? true
        : (anotherString != null)
              && (anotherString.value.length == value.length)
              && regionMatches(true, 0, anotherString, 0, value.length);
  }
public boolean regionMatches(boolean ignoreCase, int toffset,
        String other, int ooffset, int len) {
    char ta[] = value;
    int to = toffset;
    char pa[] = other.value;
    int po = ooffset;
    // Note: toffset, ooffset, or len might be near -1>>>1.
    if ((ooffset < 0) || (toffset < 0)
            || (toffset > (long)value.length - len)
            || (ooffset > (long)other.value.length - len)) {
        return false;
    }
    while (len-- > 0) {
        char c1 = ta[to++];
        char c2 = pa[po++];
        if (c1 == c2) {
            continue;
        }
        if (ignoreCase) {
            // If characters don't match but case may be ignored,
            // try converting both characters to uppercase.
            // If the results match, then the comparison scan should
            // continue.
            char u1 = Character.toUpperCase(c1);
            char u2 = Character.toUpperCase(c2);
            if (u1 == u2) {
                continue;
            }
            // Unfortunately, conversion to uppercase does not work properly
            // for the Georgian alphabet, which has strange rules about case
            // conversion.  So we need to make one last check before
            // exiting.
            if (Character.toLowerCase(u1) == Character.toLowerCase(u2)) {
                continue;
            }
        }
        return false;
    }
    return true;
}
```
OpenJDK 8u40-b25の<a href="http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/java/lang/String.java#String.compareToIgnoreCase%28java.lang.String%29" target="_blank">compareToIgnoreCaseメソッド</a><br>
compareToIgnoreCaseメソッドはletter caseを無視して比較するために次の順で判定します。
<ol>
<li>Stringと引数のStringを先頭からCharを一つずつ取り出し、両方のStringにCharが存在するインデックスまで観察する。次の条件を満たすさない場合は、そのインデックスのStringのCharから引数のStringのCharを引いた値を返す。
<ol>
<li>Stringと引数のStringの同じインデックスのCharが一致する</li>
<li>または、Stringと引数のStringの同じインデックスのCharをupper caseに変換したCharが一致する（letter caseを無視するため）</li>
<li>または、Stringと引数のStringの同じインデックスのCharをupper caseに変換したCharをlower caseに変換したCharが一致する（upper caseに変換しただけでは<a href="https://ja.wikipedia.org/wiki/%E3%82%B0%E3%83%AB%E3%82%B8%E3%82%A2%E6%96%87%E5%AD%97" target="_blank">グルジア文字</a>に対してはletter caseを無視できていないため、さらにlower caseに変換することでグルジア文字のletter caseを無視するためだそうですが、私が調べた限りではそのような問題は起こらないように見えます。参照：<a href="#コラムグルジア文字のletter-case">コラム：グルジア文字のletter case</a>。しかし、グルジア文字とは別の文字にはletter caseの変換に非対称性が存在します。参照：<a href="#コラムletter-caseの変換の非対称性">コラム：letter caseの変換の非対称性</a>。）</li>
</ol>
</li>
<li>Stringの長さから引数のStringの長さを引いた値を返す。</li>
</ol>
```java
public int compareToIgnoreCase(String str) {
    return CASE_INSENSITIVE_ORDER.compare(this, str);
}

public static final Comparator<String> CASE_INSENSITIVE_ORDER
                                     = new CaseInsensitiveComparator();
private static class CaseInsensitiveComparator
        implements Comparator<String>, java.io.Serializable {
    // use serialVersionUID from JDK 1.2.2 for interoperability
    private static final long serialVersionUID = 8575799808933029326L;

    public int compare(String s1, String s2) {
        int n1 = s1.length();
        int n2 = s2.length();
        int min = Math.min(n1, n2);
        for (int i = 0; i < min; i++) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);
            if (c1 != c2) {
                c1 = Character.toUpperCase(c1);
                c2 = Character.toUpperCase(c2);
                if (c1 != c2) {
                    c1 = Character.toLowerCase(c1);
                    c2 = Character.toLowerCase(c2);
                    if (c1 != c2) {
                        // No overflow because of numeric promotion
                        return c1 - c2;
                    }
                }
            }
        }
        return n1 - n2;
    }

    /** Replaces the de-serialized object. */
    private Object readResolve() { return CASE_INSENSITIVE_ORDER; }
}
```
---
<h3>2.2　パスフィルタ</h3>
本節では、Stringを入力してその一部を取り出すStringのパスフィルタについて説明します。
Stringのパスフィルタには、
N番目の文字を取得する方法、
ある位置からある位置までの部分文字列を取得する方法、
先頭の一文字を取得する方法、
先頭のN文字を取得する方法、
末尾の一文字を取得する方法、
末尾のN文字を取得する方法、
条件式を満たす文字や文字列を取得する方法についてそれぞれ説明します。

<h4>2.2.1　N番目の文字の取得</h4>
先頭からN番目のCharはapplyメソッド（applyは省略可能）、charAtメソッド、applyElseOptionメソッドのいずれかで取得できます。
applyElseOptionメソッドはインデックスが定義されていない場合はNoneを返し、定義されている場合はapplyメソッドの結果をSomeで包んで返します。
先頭からN番目の文字目のコードポイントはcodePointAtメソッドで取得できます。
```scala
  private val tautology: String = "ウナギはウナギだ。"

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
```
<h4>2.2.2　部分文字列の取得</h4>
Stringからインデックスからオフセットまでの範囲の部分文字列はsubstringメソッドで取得できます。
```scala
  @Test
  def testSubstring(): Unit = {
    assert(gardenPathSentence == "次郎は花子に渡した\nウナギを呼びつけた。")
    assert(gardenPathSentence.substring(10, 13) == "ウナギ")
  }
```
<h4>2.2.3　先頭の一文字の取得</h4>
先頭のCharはheadメソッドで取得できます。
headOptionメソッドは先頭のCharをSomeで包んで返します。もし先頭のCharが存在しない場合はNoneを返します。
```scala
  private val unagiCopula: String = "僕はウナギ"
  
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
Stringの先頭からN個のCharはtakeメソッドで取得できます。
substringメソッドやdropRightメソッドでも同様の処理を書くことはできます。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
末尾のCharはlastメソッドで取得できます。
lastOptionメソッドは末尾のCharをSomeで包んで返します。もし末尾のCharが存在しない場合はNoneを返します。
```scala
  private val unagiCopula: String = "僕はウナギ"
  
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
Stringの末尾からN個のCharはtakeRightメソッドで取得できます。
substringメソッドやdropメソッドでも同様の処理を書くことはできます。
```scala
  private val tautology: String = "ウナギはウナギだ。"

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
先頭から条件式を満たさなくなるまでの部分文字列はtakeWhileメソッドで取得します。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
条件を満たすCharだけ取り出して繋いだ文字列はfilterメソッドで取得できます。withFilterメソッドはfilterメソッドにより条件を満たしたCharに対して何かしらの変換処理を加えてから繋いだ文字列を返します。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
先頭から見て条件式を満たしたCharはfindメソッドで取得できます。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
partitionメソッドは条件式を満たしたCharを繋いだ文字列と条件式を満たさなかったCharを繋いだ文字列の２つを同時に返します。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
本節では、Stringを入力してその一部を削除するStringのカットフィルタについて説明します。
Stringのカットフィルタには、
N番目の文字を削除する方法、
ある位置からある位置までの部分文字列を削除する方法、
先頭の一文字を削除する方法、
先頭のN文字を削除する方法、
先頭の文字列が一致したら削除する方法、
末尾の一文字を削除する方法、
末尾のN文字を削除する方法、
末尾の文字列が一致したら削除する方法、
先頭・末尾の空白文字を削除する方法、
末尾の改行文字を削除する方法、
条件式を満たす文字や文字列を削除する方法についてそれぞれ説明します。
<h4>2.3.1　N番目の文字の削除</h4>
先頭からN番目のCharはStringBuilderのdeleteCharAtメソッドで削除します。 
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
StringBuilderのdeleteメソッドはインデックスからオフセットまでの範囲の部分文字列を削除します。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
  @Test
  def testDeleteSubstringInString(): Unit = {
    val builder: StringBuilder = new StringBuilder(tautology.length).append(tautology)
    builder.delete(3, 7)
    val str: String = builder.result
    assert(str == "ウナギだ。")
  }
```
<h4>2.3.3　先頭の一文字の削除</h4>
tailメソッドは先頭のCharを削除します。
```scala
  private val unagiCopula: String = "僕はウナギ"
  
  @Test
  def testDropHeadChar(): Unit = {
    assert(unagiCopula.tail == "はウナギ")
  }
```
<h4>2.3.4　先頭のN文字の削除</h4>
dropメソッドは先頭からN個のCharを削除します。
substringメソッドやtakeRightメソッドでも同様の処理を書くことはできます。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
stripPrefixメソッドは先頭の文字列が引数と一致したら削除します。
```scala
  private val unagiCopula: String = "僕はウナギ"
  
  @Test
  def testStripPrefix(): Unit = {
    assert("横浜国立大学".stripPrefix("横浜") == "国立大学")
    unagiCopula.stringPrefix
  }

```
<h4>2.3.6　末尾の一文字の削除</h4>
initメソッドは末尾のCharを削除します。
```scala
  private val unagiCopula: String = "僕はウナギ"
  
  @Test
  def testDropLastChar(): Unit = {
    assert(unagiCopula.init == "僕はウナ")
  }
```
<h4>2.3.7　末尾のN文字の削除</h4>
dropRightメソッドは末尾からN個のCharを削除します。
substringメソッドやtakeメソッドでも同様の処理を書くことはできます。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
stripSuffixメソッドは末尾の文字列が引数と一致したら削除します。
```scala
  @Test
  def testStripSuffix(): Unit = {
    assert("横浜国立大学".stripSuffix("立大学") == "横浜国")
  }
```
<h4>2.3.9　先頭・末尾の空白文字削除</h4>
trimメソッドは先頭や末尾の空白文字を削除します。ここでの空白文字の定義は' '（U+0020）以下のコードポイントの文字、つまりU+0000からU+0020の２１文字です。
改行文字のU+000A（"\n"）やキャリッジ・リターンのU+000D（"\r"）も削除されます。
```scala
  @Test
  def testTrim(): Unit = {
    assert(gardenPathSentence  == "次郎は花子に渡した\nウナギを呼びつけた。")
    assert(gardenPathSentence2 == " 次郎は花子に渡した\nウナギを呼びつけた。\n\n    \r")
    assert(gardenPathSentence  == gardenPathSentence2.trim)
  }
```
<h4>2.3.10　末尾改行文字削除</h4>
stripLineEndメソッドは末尾の改行文字（"\n"か"\r\n"）を一つ削除します。キャリッジ・リターン（"\r"）単体では削除されません。
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
dropWhileメソッドは先頭から条件式を満たさなくなるまでの部分文字列を削除します。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
条件を満たすCharだけ削除して繋いだ文字列はfilterNotメソッドで取得できます。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
stripMarginメソッドは各行の先頭（改行文字の次の文字）から'|'までの文字列を削除します。
stripMarginメソッドに引数を与える場合は各行の先頭から引数までの文字列を削除します。
主に改行を含む生文字リテラルのインデントを揃えるために使用されます。
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
本節では、Charの順序に基づく文字列のリストやCharのリストのソート方法について説明します。
Charによる文字列リストの正順ソート、Charによる文字列リストの逆順ソート、Charによる文字のソートについて説明します。

<h4>2.4.1　Charによる文字列リストの正順ソート</h4>
sortedメソッド、sortWithメソッド、sortByメソッドはCharの順序に基づく並び替えを行います。
sortWithメソッドでは比較の条件式そのものを与えることができます。
sortByメソッドでは比較対象を変換してから比較することができます。
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
```
<h4>2.4.2　Charによる文字列リストの逆順ソート</h4>
reverseメソッドにより逆順にソートすることができます。
sortedメソッドを通してからreverseメソッドを通すことで逆順にソートすることができます。
（reverseメソッドを通してからsortedメソッドを通すと単純にsortedメソッドを通したものと同じ順序になりますので、処理を通す順序に気をつける必要があります。）
sortWithメソッドで逆順になる方に条件式を与えることで逆順にソートすることができます。
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
```
<h4>2.4.3　Charによる文字のソート</h4>
Charによる文字のソートもsortedメソッドやreverseメソッドを使用することができます。Char単位でソートするとサロゲートペアがある場合は文字が壊れてしまう危険性があります。ちなみに、StringBuilderクラスにJava由来のreverseメソッドがあります。
```scala
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
```
---
<h3>2.5　集合演算</h3>
本節では、Charのリスト（String）や文字列のリストに対する集合演算について説明します。
Charのリストや文字列のリストから、
最大・最小の要素を取り出す方法、
String内のCharの総和・相乗を求める方法、
Charのリストや文字列のリストの２つのリストの和集合、差集合、重複を排除した集合、積集合を求める方法、
Charのリストや文字列のリストの組合せ・順列を求める方法を説明します。
<h4>2.5.1　最大</h4>
Charの順序で最大の要素をmaxメソッドで取り出すことができます。
maxByメソッドで要素を変換した上で最大の要素を取り出すことができます。
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
    def transformString(str: String): Int = {
      str.length
    }
  }
```
<h4>2.5.2　最小</h4>
Charの順序で最小の要素をmaxメソッドで取り出すことができます。
maxByメソッドで要素を変換した上で最小の要素を取り出すことができます。
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
```
<h4>2.5.3　総和</h4>
sumメソッドでString内のCharの整数値の総和を得ることができます。
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
<h4>2.5.4　相乗</h4>
productメソッドでString内のCharの整数値の相乗を得ることができます。
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
<h4>2.5.5　和集合</h4>
unionメソッドにより文字列の結合ができますが、concatや+演算子の方が高速です。
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
<h4>2.5.6　差集合</h4>
diffメソッドで引数として与えた文字をStringの先頭から順に削除していくことができます。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
<h4>2.5.7　重複排除</h4>
distinctメソッドで重複を排除することができます。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
  @Test
  def testDistinct(): Unit = {
    assert(tautology == "ウナギはウナギだ。")
    assert(tautology.distinct == "ウナギはだ。")

    val list: Seq[String] = Seq[String]("安", "衣", "安", "安", "宇", "以", "於", "𠮷")
    assert(list.distinct == Seq[String]("安", "衣", "宇", "以", "於", "𠮷"))
  }
```
<h4>2.5.8　積集合</h4>
intersectメソッドでLCS (Longest Common Subsequence)を得ることができます。
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
<h4>2.5.9　組合せ</h4>
combinationsメソッドで組合せを得ることができます。
```scala
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
```
<h4>2.5.10　順列</h4>
permutationsメソッドで順列を得ることができます。
```scala
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
```
---
<h3>2.6　インデックス</h3>
コレクションのデータ構造はインデックスで操作すること（ランダムアクセス）を目的とした配列とイテレーションで操作すること（シーケンシャルアクセス）を目的とした連結リストに大きく分けられます。Stringの実体はCharの配列ですので、イテレーションよりインデックスで操作する方が基本的には望ましいデータ構造です。配列と連結リストについては、<a href="#コラム配列と連結リスト">コラム：配列と連結リスト</a>を参照してください。本節では、Stringのインデックスに関わる処理について説明します。
正方向（逆方向）に解析して最初に現れたインデックスを取得する方法、
正方向（逆方向）に解析して条件を最初に満たしたインデックスを取得する方法、
インデックスを全て取得する方法、
インデックスが定義されているかを確認する方法について説明します。

<h4>2.6.1　正方向に解析して最初に現れたインデックスを取得</h4>
indexOfメソッドで先頭から見て最初に与えた引数（CharまたはString）が現れた位置を取得できます。
indexOfメソッドに第二引数として探索開始位置を与えることも可能です。
indexOfSliceメソッドもindexOfメソッドと同様の結果を返しますが、実装されているアルゴリズムが異なります。
詳しくは、前章の<a href="https://github.com/ynupc/scalastringcourseday6/blob/master/doc/regex.md#113部分一致表層文字列">1.1.3　部分一致（表層文字列）</a>を読んでください。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
```
<h4>2.6.2　逆方向に解析して最初に現れたインデックスを取得</h4>
lastIndexOfメソッドで末尾から見て最初に与えた引数（CharまたはString）が現れた位置を取得できます。
lastIndexOfメソッドに第二引数として探索開始位置を与えることも可能です。
lastIndexOfSliceメソッドもlastIndexOfメソッドと同様の結果を返しますが、実装されているアルゴリズムが異なります。
詳しくは、前章の<a href="https://github.com/ynupc/scalastringcourseday6/blob/master/doc/regex.md#113部分一致表層文字列">1.1.3　部分一致（表層文字列）</a>を読んでください。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
```
<h4>2.6.3　正方向に解析して条件を最初に満たしたインデックスを取得</h4>
prefixLengthメソッドで先頭から見て最初に与えた条件式を満たした位置を取得できます。
segmentLengthメソッドで与えた探索開始位置から見て最初に与えた条件式を満たした位置までの長さを返します。例えば、探索開始位置が1で条件式を満たした位置が2の場合は長さは1（=2-1）です。
indexWhereメソッドで先頭から見て最初に与えた条件式を満たした位置を取得できます。
indexWhereメソッドに第二引数として探索開始位置を与えることも可能です。
```scala
  private val tautology: String = "ウナギはウナギだ。"
  
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
```
<h4>2.6.4　逆方向に解析して条件を最初に満たしたインデックスを取得</h4>
indexWhereメソッドで末尾から見て最初に与えた条件式を満たした位置を取得できます。
indexWhereメソッドに第二引数として探索開始位置を与えることも可能です。
```scala
  private val tautology: String = "ウナギはウナギだ。"

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
```
<h4>2.6.5　インデックスを全て取得</h4>
indicesメソッドで全てのインデックスを取得できます。これは主にfor文でfor (i &lt;- 0 until str.length)と表記する場合にfor (i &lt;- str.indices)と簡略に書く目的で使用されます。
```scala
  private val tautology: String = "ウナギはウナギだ。"

  @Test
  def testIndices(): Unit = {
    val indices: Range = tautology.indices
    assert(indices.nonEmpty)
    assert(indices.start == 0)
    assert(indices.end   == 9)
    assert(indices.step  == 1)
    assert(indices == Seq[Int](0, 1, 2, 3, 4, 5, 6, 7, 8))
  }
```
zipWithIndexメソッドで文字列内のCharとインデックスの対を全て取得することができます。
```scala
  private val tautology: String = "ウナギはウナギだ。"

  @Test
  def testZipWithIndex(): Unit = {
    assert(tautology.zipWithIndex == Seq[(Char, Int)](
      ('ウ', 0), ('ナ', 1), ('ギ', 2), ('は', 3),
      ('ウ', 4), ('ナ', 5), ('ギ', 6), ('だ', 7), ('。', 8)))
  }
```
<h4>2.6.6　インデックスが定義されているか</h4>
isDefinedAtメソッドでインデックスが定義されているかを確認することができます。
```scala
  private val tautology: String = "ウナギはウナギだ。"

  @Test
  def testIsDefinedAt(): Unit = {
    assert(!tautology.isDefinedAt(-1))
    assert(tautology.isDefinedAt(0))
    assert(tautology.isDefinedAt(tautology.length - 1))
    assert(!tautology.isDefinedAt(tautology.length))
  }
```
---
<h3>2.7　イテレーション</h3>
コレクションのデータ構造はインデックスで操作すること（ランダムアクセス）を目的とした配列とイテレーションで操作すること（シーケンシャルアクセス）を目的とした連結リストに大きく分けられます。Stringの実体はCharの配列ですのでイテレーションよりインデックスによる処理が基本的には望ましいです。SeqやListは連結リストですので、イテレーションによる操作が基本的には望ましいです。配列と連結リストについては、<a href="#コラム配列と連結リスト">コラム：配列と連結リスト</a>を参照してください。
本節では、イテレーションに関する処理を説明します。
イテレータを生成する方法、
写像する方法、
N-gramを生成する方法、
Stringを行に分割する方法について説明します。
<h4>2.7.1　イテレータ</h4>
```scala
  private val unagiCopula: String = "僕はウナギ"
  
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
```
```scala
  @Test
  def testIterator(): Unit = {
    assert(unagiCopula.iterator.toSeq == Seq[Char]('僕', 'は', 'ウ', 'ナ', 'ギ'))
  }

  @Test
  def testReverseIterator(): Unit = {
    assert(unagiCopula.reverseIterator.toSeq == Seq[Char]('ギ', 'ナ', 'ウ', 'は', '僕'))
  }
```
<h4>2.7.2　写像</h4>

```scala
  private val unagiCopula: String = "僕はウナギ"

  @Test
  def testMap(): Unit = {
    assert(unagiCopula.map(char => char) == "僕はウナギ")
  }

  @Test
  def testReverseMap(): Unit = {
    assert(unagiCopula.reverseMap(char => char) == "ギナウは僕")
  }
```
<h4>2.7.3　N-gramの生成</h4>
```scala
  private val unagiCopula: String = "僕はウナギ"
  
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
```
<h4>2.7.4　行分割</h4>
```scala
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
```
---
<h3>2.8　文字列間の文字に注目した類似度</h3>
有名なアルゴリズムを用いた文字列間の文字に注目した類似度の計算方法について説明します。今回は文字を単位に文字列間の表層的な類似度を計算しますが、単位を文字ではなく短単位形態素N-gram、長単位形態素N-gram、内容語N-gram、Basic Element N-gramなど意味的な概念単位に変えることでアルゴリズムはそのまま用いてより意味的な類似度を計算することができます。意味的な類似度を得るためには、概念単位を用いて今回のアルゴリズムを適用させる以外にも、WordNetのようなシソーラスやWord2Vecのような分散表現ベクトルや含意関係認識や述語項構造や主題役割、TF-IDF、Okapi BM25を用いる方法などがあります。文字列間の意味的な類似度については今回のコースでは扱いません。
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
<h4>2.8.2　文字LCSによるF1値</h4>
LCSの長さで類似度を計測することができます。
```scala
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

  private def divide(numerator: Double, denominator: Double): Double = {
    if (denominator == 0) {
      return 0D
    }
    numerator / denominator
  }
```
<h4>2.8.3　文字N-gramによるF1値</h4>
文字N-gramの重なりによって類似度を計測することができます。
```scala
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
  
  private def divide(numerator: Double, denominator: Double): Double = {
    if (denominator == 0) {
      return 0D
    }
    numerator / denominator
  }
```
<h4>2.8.4　ハミング距離（自作）</h4>
等しい文字数を持つ２つの文字列の中で、対応する位置にある異なった文字の数です。
```scala
  @Test
  def testHammingDistance(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    assert(HammingDistance.calculate(source, target.substring(0, source.length)) == 0.7272727272727273D)
  }
```
<h4>2.8.5　レーベンシュタイン距離（自作）</h4>
１つの文字列を別の文字列に変形するのに必要な操作の最小回数です。
操作は文字の挿入（insertion）、削除（deletion）、置換（substitution）の３つです。
```scala
  @Test
  def testLevenshteinDistance(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    assert(LevenshteinDistance.calculate(source, target) == 0.3846153846153846D)
  }
```
<h4>2.8.6　ダメラオウ・レーベンシュタイン距離（自作）</h4>
１つの文字列を別の文字列に変形するのに必要な操作の最小回数です。
操作は文字の挿入（insertion）、削除（deletion）、置換（substitution）、交換（transposition）の４つです。
```scala
  @Test
  def testDamerauLevenshteinDistance(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    assert(DamerauLevenshteinDistance.calculate(source, target) == 0.3846153846153846D)
  }
```
<h4>2.8.7　ジャロ距離（自作）</h4>
ジャロ距離djは文字列s1、s2において次の３つの類似度の平均です。s1とs2に一致する文字数mとすると、
<ul>
<li>m/|s1|</li>
<li>m/|s2|</li>
<li>s1とs2の不一致部分で、それぞれで交換するとs1とs2が一致する文字数の合計を２で割った値tとする。例えば、MARTHAとMARHTAの場合はH/TとT/Hの二文字を２で割って、t=1。このとき、(m-t)/m</li>
</ul>
```scala
  @Test
  def testJaroDistance(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    assert(JaroDistance.calculate(source, target) == 0.668997668997669D)
  }
```
<h4>2.8.8　ジャロ・ウィンクラー距離（自作）</h4>
ジャロ・ウィンクラー距離は、先頭の方の文字列が末尾の方の文字列より重要視したジャロ距離の拡張です。
```scala
  @Test
  def testJaroWinklerDistance(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    assert(JaroWinklerDistance.calculate(source, target) == 0.668997668997669D)
  }
```
<h4>2.8.9　ベクトル化（自作）</h4>
数え上げてベクトルを作成する場合は、頻度ベクトルと二値ベクトルの２種類のベクトルが作成できます。
<table>
<tr><th>ベクトルの種類</th><th>説明</th></tr>
<tr><td>頻度ベクトル</td><td>重複したら頻度としてカウントして作成するベクトル</td></tr>
<tr><td>二値ベクトル</td><td>重複してもカウントしないので、存在するか否かの二値で作成するベクトル</td></tr>
</table>
<h4>2.8.10　ベクトル間の類似度（自作）</h4>
ベクトル間の類似度を計算するプログラムの例を次に示します。
```scala
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
```
<h4>2.8.11　ベクトル間の距離（自作）</h4>
ベクトル間の非類似度を計算するプログラムの例を次に示します。
```scala
  @Test
  def testBagOfBigramsEuclideanDistance(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    val vector1: FrequencyVector = FrequencyVectorGenerator.getVectorFromText(source)
    val vector2: FrequencyVector = FrequencyVectorGenerator.getVectorFromText(target)

    assert(Dissimilarity.calculateEuclidean(vector1, vector2) == 4.0D)
  }
```
<h4>2.8.12　ベクトル間の包含度（自作）</h4>
ベクトル間の包含度を計算するプログラムの例を次に示します。
```scala
  @Test
  def testBigramsInclusion(): Unit = {
    val source: String = "$ウウ$ナナ$ギギ$は"
    val target: String = "ウウ#ナナ#ギギ#だ#。#"

    val vector1: BinaryVector = BinaryVectorGenerator.getVectorFromText(source)
    val vector2: BinaryVector = BinaryVectorGenerator.getVectorFromText(target)

    assert(OverlapCalculator.calculate(vector1, vector2) == 0.3D)
  }
```
<h4>2.8.13　文字列間の類似度・非類似度・包含度に関する表</h4>
文字列間の類似度・非類似度・包含度についての有名なアルゴリズムについて次の表にまとめました。私が実装したプログラムの例がリンクしていますので参照してください。
<table>
<tr>
<th>名前</th>
<th>類似度・非類似度</th>
<th>値の正規性</th>
<th>変数の単位</th>
<th>変数の交換可能性</th>
<th>ソースコード</th>
</tr>
<tr>
<td>ハミング距離</td>
<td>非類似度</td>
<td>正規</td>
<td>シーケンス</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/HammingDistance.scala">text.similarity.HammingDistance</a></td>
</tr>
<tr>
<td>レーベンシュタイン距離</td>
<td>非類似度</td>
<td>正規</td>
<td>シーケンス</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/LevenshteinDistance.scala">text.similarity.LevenshteinDistance</a></td>
</tr>
<tr>
<td>ダメラオウ・レーベンシュタイン距離</td>
<td>非類似度</td>
<td>正規</td>
<td>シーケンス</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/DamerauLevenshteinDistance.scala">text.similarity.DamerauLevenshteinDistance</a></td>
</tr>
<tr>
<td>ジャロ距離</td>
<th>類似度</th>
<td>正規</td>
<td>シーケンス</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/JaroWinklerDistance.scala">text.similarity.JaroDistance</a></td>
</tr>
<tr>
<td>ジャロ・ウィンクラー距離</td>
<th>類似度</th>
<td>正規</td>
<td>シーケンス</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/JaroWinklerDistance.scala">text.similarity.JaroWinklerDistance</a></td>
</tr>
<tr>
<td>コサイン類似度</td>
<td>類似度</td>
<td>正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>共分散</td>
<td>類似度</td>
<td>正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>ダイス係数</td>
<td>類似度</td>
<td>正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>内積</td>
<td>類似度</td>
<td>非正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>ヤッカード係数</td>
<td>類似度</td>
<td>正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>ヤッカード・シンプソン係数</td>
<td>類似度</td>
<td>正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>リン98類似度</td>
<td>類似度</td>
<td>正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>ミハルシア04類似度</td>
<td>類似度</td>
<td>非正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>ピアソンの積率相関係数</td>
<td>類似度</td>
<td>正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>チェビシェフ距離</td>
<td>非類似度</td>
<td>非正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Dissimilarity.scala">text.similarity.Dissimilarity</a></td>
</tr>
<tr>
<td>チェビシェフ距離の逆数</td>
<td>類似度</td>
<td>正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>ユークリッド距離</td>
<td>非類似度</td>
<td>非正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Dissimilarity.scala">text.similarity.Dissimilarity</a></td>
</tr>
<tr>
<td>ユークリッド距離の逆数</td>
<td>類似度</td>
<td>正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>マンハッタン距離</td>
<td>非類似度</td>
<td>非正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Dissimilarity.scala">text.similarity.Dissimilarity</a></td>
</tr>
<tr>
<td>マンハッタン距離の逆数</td>
<td>類似度</td>
<td>正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>ミンコフスキー距離</td>
<td>非類似度</td>
<td>非正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Dissimilarity.scala">text.similarity.Dissimilarity</a></td>
</tr>
<tr>
<td>ミンコフスキー距離の逆数</td>
<td>類似度</td>
<td>正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>シンプソン係数</td>
<td>類似度</td>
<td>正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>トベルスキー係数</td>
<td>類似度</td>
<td>正規</td>
<td>頻度ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Similarity.scala">text.similarity.Similarity</a></td>
</tr>
<tr>
<td>LCSのF値</td>
<td>類似度</td>
<td>正規</td>
<td>シーケンス</td>
<td>交換不可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Overlap.scala">text.similarity.Overlap</a></td>
</tr>
<tr>
<td>LCSのF1値</td>
<td>類似度</td>
<td>正規</td>
<td>シーケンス</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Overlap.scala">text.similarity.Overlap</a></td>
</tr>
<tr>
<td>LCSの適合率（Precision）</td>
<td>類似度</td>
<td>正規</td>
<td>シーケンス</td>
<td>交換不可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Overlap.scala">text.similarity.Overlap</a></td>
</tr>
<tr>
<td>LCSの再現率（Recall）</td>
<td>類似度</td>
<td>正規</td>
<td>シーケンス</td>
<td>交換不可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Overlap.scala">text.similarity.Overlap</a></td>
</tr>
<tr>
<td>F値</td>
<td>類似度</td>
<td>正規</td>
<td>二値ベクトル</td>
<td>交換不可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Overlap.scala">text.similarity.Overlap</a></td>
</tr>
<tr>
<td>F1値</td>
<td>類似度</td>
<td>正規</td>
<td>二値ベクトル</td>
<td>交換可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Overlap.scala">text.similarity.Overlap</a></td>
</tr>
<tr>
<td>適合率（Precision）</td>
<td>類似度</td>
<td>正規</td>
<td>二値ベクトル</td>
<td>交換不可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Overlap.scala">text.similarity.Overlap</a></td>
</tr>
<tr>
<td>再現率（Recall）</td>
<td>類似度</td>
<td>正規</td>
<td>二値ベクトル</td>
<td>交換不可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Overlap.scala">text.similarity.Overlap</a></td>
</tr>
<tr>
<td>Rus05包含度</td>
<td>類似度</td>
<td>正規</td>
<td>二値ベクトル</td>
<td>交換不可能</td>
<td><a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/main/scala/text/similarity/Overlap.scala">text.similarity.Overlap</a></td>
</tr>
</table>

***
<h3>コラム：グルジア文字のletter case</h3>
中世に開発されたグルジア文字はかつてupper caseとlower caseの２種類の文字セットが存在しましたが、後に、２つの文字セットに代わり、１つの文字セットが用いられるようになりました。
かつてはクツリ（Khutsuri）と呼ばれる文字がキリスト教で使用されていました。クツリは２つのletter caseを持っており、upper caseはムルグロヴァニ（Mrgvlovani「丸文字」。別名Asomtavruli。）、lower caseはヌスフリ（Nuskhuri「目録文字」）と呼ばれます。ムルグロヴァニは西暦430年頃から使用されていました。９世紀の間に、徐々にムルグロヴァニがヌスフリに置き換えられていきました。今日使用されている文字はムヘドルリ（Mkhedruli「騎兵文字」）と呼ばれるもので１１世紀から１３世紀の間に開発され現在も使用されています。このようにグルジア文字には３種類の文字セットが存在します。
ムルグロヴァニとヌスフリはupper caseとlower caseの関係にあり、ムヘドルリはletter caseが１種類しかないためtoUpperCaseメソッドを使ってもtoLowerCaseメソッドを使っても文字は変わりません。
StringクラスのequalsIgnoreCaseメソッドやcompareToIgnoreCaseメソッドでCharacter.toUpperCaseメソッドでupper caseに変換した後Character.toLowerCaseメソッドでlower caseに変換してletter caseが無視できているかをグルジア文字のために念入りにチェックしているとOpenJDKのJavadocに書かれていますが、私が調べた限りでは（調査に使った<a href="https://github.com/ynupc/scalastringcourseday6/blob/master/src/test/scala/Day6GeorgianAlphabetTestSuite.scala">ソースコード</a>）それをする必要はないようです。
<table>
<tr>
<th rowspan="2">読み</th><th colspan="2">bicameral alphabet</th><th>unicameral alphabet</th><th rowspan="2">備考</th>
</tr>
<tr>
<th>ムルグロヴァニ<br>（upper case）</th><th>ヌスフリ<br>（lower case）</th><th>ムヘドルリ</th>
</tr>
<tr>
<td>ani</td><td>Ⴀ</td><td>ⴀ</td><td>ა</td><td>&nbsp;</td>
</tr>
<tr>
<td>bani</td><td>Ⴁ</td><td>ⴁ</td><td>ბ</td><td>&nbsp;</td>
</tr>
<tr>
<td>gani</td><td>Ⴂ</td><td>ⴂ</td><td>გ</td><td>&nbsp;</td>
</tr>
<tr>
<td>doni</td><td>Ⴃ</td><td>ⴃ</td><td>დ</td><td>&nbsp;</td>
</tr>
<tr>
<td>eni</td><td>Ⴄ</td><td>ⴄ</td><td>ე</td><td>&nbsp;</td>
</tr>
<tr>
<td>vini</td><td>Ⴅ</td><td>ⴅ</td><td>ვ</td><td>&nbsp;</td>
</tr>
<tr>
<td>zeni</td><td>Ⴆ</td><td>ⴆ</td><td>ზ</td><td>&nbsp;</td>
</tr>
<tr>
<td>he</td><td>Ⴡ</td><td>ⴡ</td><td>ჱ</td><td>現在は不使用</td>
</tr>
<tr>
<td>tani</td><td>Ⴇ</td><td>ⴇ</td><td>თ</td><td>&nbsp;</td>
</tr>
<tr>
<td>ini</td><td>Ⴈ</td><td>ⴈ</td><td>ი</td><td>&nbsp;</td>
</tr>
<tr>
<td>k'ani</td><td>Ⴉ</td><td>ⴉ</td><td>კ</td><td>&nbsp;</td>
</tr>
<tr>
<td>lasi</td><td>Ⴊ</td><td>ⴊ</td><td>ლ</td><td>&nbsp;</td>
</tr>
<tr>
<td>mani</td><td>Ⴋ</td><td>ⴋ</td><td>მ</td><td>&nbsp;</td>
</tr>
<tr>
<td>nari</td><td>Ⴌ</td><td>ⴌ</td><td>ნ</td><td>&nbsp;</td>
</tr>
<tr>
<td>hie</td><td>Ⴢ</td><td>ⴢ</td><td>ჲ</td><td>現在は不使用</td>
</tr>
<tr>
<td>oni</td><td>Ⴍ</td><td>ⴍ</td><td>ო</td><td>&nbsp;</td>
</tr>
<tr>
<td>p'ari</td><td>Ⴎ</td><td>ⴎ</td><td>პ</td><td>&nbsp;</td>
</tr>
<tr>
<td>zhani</td><td>Ⴏ</td><td>ⴏ</td><td>ჟ</td><td>&nbsp;</td>
</tr>
<tr>
<td>rae</td><td>Ⴐ</td><td>ⴐ</td><td>რ</td><td>&nbsp;</td>
</tr>
<tr>
<td>sani</td><td>Ⴑ</td><td>ⴑ</td><td>ს</td><td>&nbsp;</td>
</tr>
<tr>
<td>t'ari</td><td>Ⴒ</td><td>ⴒ</td><td>ტ</td><td>&nbsp;</td>
</tr>
<tr>
<td>vie</td><td>Ⴣ</td><td>ⴣ</td><td>ჳ</td><td>現在は不使用</td>
</tr>
<tr>
<td rowspan="2">uni</td><td>ႭჃ</td><td>ⴍⴣ </td><td rowspan="2">უ</td><td rowspan="2">&nbsp;</td>
</tr>
<tr>
<td>Ⴓ</td><td>ⴓ</td>
</tr>
<tr>
<td>pari</td><td>Ⴔ</td><td>ⴔ</td><td>ფ</td><td>&nbsp;</td>
</tr>
<tr>
<td>kani</td><td>Ⴕ</td><td>ⴕ</td><td>ქ</td><td>&nbsp;</td>
</tr>
<tr>
<td>ghani</td><td>Ⴖ</td><td>ⴖ</td><td>ღ</td><td>&nbsp;</td>
</tr>
<tr>
<td>q'ari</td><td>Ⴗ</td><td>ⴗ</td><td>ყ</td><td>&nbsp;</td>
</tr>
<tr>
<td>shini</td><td>Ⴘ</td><td>ⴘ</td><td>შ</td><td>&nbsp;</td>
</tr>
<tr>
<td>chini</td><td>Ⴙ</td><td>ⴙ</td><td>ჩ</td><td>&nbsp;</td>
</tr>
<tr>
<td>tsani</td><td>Ⴚ</td><td>ⴚ</td><td>ც</td><td>&nbsp;</td>
</tr>
<tr>
<td>dzili</td><td>Ⴛ</td><td>ⴛ</td><td>ძ</td><td>&nbsp;</td>
</tr>
<tr>
<td>ts'ili</td><td>Ⴜ</td><td>ⴜ</td><td>წ</td><td>&nbsp;</td>
</tr>
<tr>
<td>ch'ari</td><td>Ⴝ</td><td>ⴝ</td><td>ჭ</td><td>&nbsp;</td>
</tr>
<tr>
<td>khani</td><td>Ⴞ</td><td>ⴞ</td><td>ხ</td><td>&nbsp;</td>
</tr>
<tr>
<td>qari</td><td>Ⴤ</td><td>ⴤ</td><td>ჴ</td><td>現在は不使用</td>
</tr>
<tr>
<td>jani</td><td>Ⴟ</td><td>ⴟ</td><td>ჯ</td><td>&nbsp;</td>
</tr>
<tr>
<td>hae</td><td>Ⴠ</td><td>ⴠ</td><td>ჰ</td><td>&nbsp;</td>
</tr>
<tr>
<td>hoe</td><td>Ⴥ</td><td>ⴥ</td><td>ჵ</td><td>現在は不使用</td>
</tr>
</table>
***
<h3>コラム：letter caseの変換の非対称性</h3>
<h4>（１）LATIN CAPITAL LETTER IとLATIN CAPITAL LETTER I WITH DOT ABOVE</h4>
LATIN CAPITAL LETTER IとLATIN CAPITAL LETTER I WITH DOT ABOVEをletter caseを無視した時に同一視したいとすると、それらはどちらもupper caseなのでtoUpperCaseメソッドを実行しても変わらないため等値判定してもfalseが返ります。これらのlower caseはいずれもLATIN SMALL LETTER Iなので、等値判定するとtrueが返ります。なお、Day 7で解説するUnicode文字正規化をLATIN CAPITAL LETTER IとLATIN CAPITAL LETTER I WITH DOT ABOVEに対してそれぞれ行ってもどちらも変化しません。従って、LATIN CAPITAL LETTER IとLATIN CAPITAL LETTER I WITH DOT ABOVEを同一視するためにはlower caseに変換する必要があります。
```scala
  @Test
  def testI(): Unit = {
    val char73:  Char = 73.toChar  //LATIN CAPITAL LETTER I
    val char304: Char = 304.toChar //LATIN CAPITAL LETTER I WITH DOT ABOVE

    printf("%s %d%n", char73, char73.toInt)
    printf("%s %d%n", char304, char304.toInt)
    assert(char73 == 'I')
    assert(char73.toInt == 73)
    assert(char304 == 'İ')
    assert(char304.toInt == 304)
    println("toUpperCase")
    val char73UpperCase: Char = Character.toUpperCase(char73)
    val char304UpperCase: Char = Character.toUpperCase(char304)
    printf("%s %d%n", char73UpperCase, char73UpperCase.toInt)
    printf("%s %d%n", char304UpperCase, char304UpperCase.toInt)
    assert(char73UpperCase == 'I')
    assert(char73UpperCase.toInt == 73)
    assert(char304UpperCase == 'İ')
    assert(char304UpperCase.toInt == 304)
    println("toLowerCase")
    val char73LowerCase: Char = Character.toLowerCase(char73)
    val char304LowerCase: Char = Character.toLowerCase(char304)
    printf("%s %d%n", char73LowerCase, char73LowerCase.toInt)
    printf("%s %d%n", char304LowerCase, char304LowerCase.toInt)
    assert(char73LowerCase == 'i')
    assert(char73LowerCase.toInt == 105) //LATIN SMALL LETTER I
    assert(char304LowerCase == 'i')
    assert(char304LowerCase.toInt == 105)
    println("toUpperCase.toLowerCase")
    val char73UpperCaseLowerCase: Char = Character.toLowerCase(char73UpperCase)
    val char304UpperCaseLowerCase: Char = Character.toLowerCase(char304UpperCase)
    printf("%s %d%n", char73UpperCaseLowerCase, char73UpperCaseLowerCase.toInt)
    printf("%s %d%n", char304UpperCaseLowerCase, char304UpperCaseLowerCase.toInt)
    assert(char73UpperCaseLowerCase == 'i')
    assert(char73UpperCaseLowerCase.toInt == 105)
    assert(char304UpperCaseLowerCase == 'i')
    assert(char304UpperCaseLowerCase.toInt == 105)
    println("toLowerCase.toUpperCase")
    val char73LowerCaseUpperCase: Char = Character.toUpperCase(char73LowerCase)
    val char304LowerCaseUpperCase: Char = Character.toUpperCase(char304LowerCase)
    printf("%s %d%n", char73LowerCaseUpperCase, char73LowerCaseUpperCase.toInt)
    printf("%s %d%n", char304LowerCaseUpperCase, char304LowerCaseUpperCase.toInt)
    assert(char73LowerCaseUpperCase == 'I')
    assert(char73LowerCaseUpperCase.toInt == 73)
    assert(char304LowerCaseUpperCase == 'I')
    assert(char304LowerCaseUpperCase.toInt == 73)
    println("Normalize with the NFKC form")
    val char73Normalized: Char = Normalizer.normalize(char73.toString, Normalizer.Form.NFKC).head
    val char304Normalized: Char = Normalizer.normalize(char304.toString, Normalizer.Form.NFKC).head
    printf("%s %d%n", char73Normalized, char73Normalized.toInt)
    printf("%s %d%n", char304Normalized, char304Normalized.toInt)
    assert(char73Normalized == 'I')
    assert(char73Normalized.toInt == 73)
    assert(char304Normalized == 'İ')
    assert(char304Normalized.toInt == 304)
    println("Normalize with the NFKC form toUpperCase")
    val char73NormalizedUpperCase: Char = Character.toUpperCase(char73Normalized)
    val char304NormalizedUpperCase: Char = Character.toUpperCase(char304Normalized)
    printf("%s %d%n", char73NormalizedUpperCase, char73NormalizedUpperCase.toInt)
    printf("%s %d%n", char304NormalizedUpperCase, char304NormalizedUpperCase.toInt)
    assert(char73NormalizedUpperCase == 'I')
    assert(char73NormalizedUpperCase.toInt == 73)
    assert(char304NormalizedUpperCase == 'İ')
    assert(char304NormalizedUpperCase.toInt == 304)
    println("toUpperCase Normalize with the NFKC form")
    val char73UpperCaseNormalized: Char = Normalizer.normalize(char73UpperCase.toString, Normalizer.Form.NFKC).head
    val char304UpperCaseNormalized: Char = Normalizer.normalize(char304UpperCase.toString, Normalizer.Form.NFKC).head
    printf("%s %d%n", char73UpperCaseNormalized, char73UpperCaseNormalized.toInt)
    printf("%s %d%n", char304UpperCaseNormalized, char304UpperCaseNormalized.toInt)
    println("Normalize with the NFKC form toLowerCase")
    assert(char73UpperCaseNormalized == 'I')
    assert(char73UpperCaseNormalized.toInt == 73)
    assert(char304UpperCaseNormalized == 'İ')
    assert(char304UpperCaseNormalized.toInt == 304)
    val char73NormalizedLowerCase: Char = Character.toLowerCase(char73Normalized)
    val char304NormalizedLowerCase: Char = Character.toLowerCase(char304Normalized)
    printf("%s %d%n", char73NormalizedLowerCase, char73NormalizedLowerCase.toInt)
    printf("%s %d%n", char304NormalizedLowerCase, char304NormalizedLowerCase.toInt)
    assert(char73NormalizedLowerCase == 'i')
    assert(char73NormalizedLowerCase.toInt == 105)
    assert(char304NormalizedLowerCase == 'i')
    assert(char304NormalizedLowerCase.toInt == 105)
    println("toLowerCase Normalize with the NFKC form")
    val char73LowerCaseNormalized: Char = Normalizer.normalize(char73LowerCase.toString, Normalizer.Form.NFKC).head
    val char304LowerCaseNormalized: Char = Normalizer.normalize(char304LowerCase.toString, Normalizer.Form.NFKC).head
    printf("%s %d%n", char73LowerCaseNormalized, char73LowerCaseNormalized.toInt)
    printf("%s %d%n", char304LowerCaseNormalized, char304LowerCaseNormalized.toInt)
    assert(char73LowerCaseNormalized == 'i')
    assert(char73LowerCaseNormalized.toInt == 105)
    assert(char304LowerCaseNormalized == 'i')
    assert(char304LowerCaseNormalized.toInt == 105)
    println("---")

    assert(char73 != char304)
    //toUpperCase
    assert(Character.toUpperCase(char73) != Character.toUpperCase(char304))
    //toLowerCase
    assert(Character.toLowerCase(char73) == Character.toLowerCase(char304))
    //toUpperCase.toLowerCase
    assert(Character.toLowerCase(Character.toUpperCase(char73)) == Character.toLowerCase(Character.toUpperCase(char304)))
    //toLowerCase.toUpperCase
    assert(Character.toUpperCase(Character.toLowerCase(char73)) == Character.toUpperCase(Character.toLowerCase(char304)))
    //normalize with the NFKC
    assert(Normalizer.normalize(char73.toString, Normalizer.Form.NFKC) != Normalizer.normalize(char304.toString, Normalizer.Form.NFKC))
    //normalize with the NFKC form toUpperCase
    assert(Normalizer.normalize(char73.toString, Normalizer.Form.NFKC).toUpperCase != Normalizer.normalize(char304.toString, Normalizer.Form.NFKC).toUpperCase)
    //toUpperCase normalize with the NFKC form
    assert(Normalizer.normalize(Character.toUpperCase(char73).toString, Normalizer.Form.NFKC) != Normalizer.normalize(Character.toUpperCase(char304).toString, Normalizer.Form.NFKC))
    //normalize with the NFKC form toLowerCase
    assert(Normalizer.normalize(char73.toString, Normalizer.Form.NFKC).toLowerCase != Normalizer.normalize(char304.toString, Normalizer.Form.NFKC).toLowerCase)
    //toLowerCase normalize with the NFKC form
    assert(Normalizer.normalize(Character.toLowerCase(char73).toString, Normalizer.Form.NFKC) == Normalizer.normalize(Character.toLowerCase(char304).toString, Normalizer.Form.NFKC))
  }
```
<h4>（２）GREEK THETA SYMBOLとGREEK CAPITAL THETA SYMBOL</h4>
GREEK THETA SYMBOLとGREEK CAPITAL THETA SYMBOLをletter caseを無視した時に同一視したいとすると、GREEK THETA SYMBOLはlower caseでGREEK CAPITAL THETA SYMBOLはupper caseであり、GREEK THETA SYMBOLのupper caseはGREEK CAPITAL LETTER THETA、GREEK CAPITAL THETA SYMBOLのlower caseはGREEK SMALL LETTER THETAであるため、一度upper caseやlower caseに揃えただけでは一致しません。ところが、GREEK CAPITAL LETTER THETAとGREEK SMALL LETTER THETAはbicameralな関係にあり、つまりGREEK CAPITAL LETTER THETAのlower caseがGREEK SMALL LETTER、GREEK SMALL LETTERのupper caseがGREEK CAPITAL LETTER THETAであるため、GREEK THETA SYMBOLとGREEK CAPITAL THETA SYMBOLはlower caseへの変換とupper caseへの変換の２回変換を行えば、その変換の順序に関係なく一致を見ることができます。
ちなみに、Day 7で解説するUnicode文字正規化をGREEK THETA SYMBOLとGREEK CAPITAL THETA SYMBOLに行うと、それぞれGREEK SMALL LETTERとGREEK CAPITAL LETTER THETAに変換されるため、lower caseへの変換とUnicode文字正規化、あるいはupper caseへの変換とUnicode文字正規化を行っても、これらも変換の順序に関係なく一致を見ることができます。
```scala
  @Test
  @Test
  def testTheta(): Unit = {
    val char977: Char = 977.toChar //GREEK THETA SYMBOL
    val char1012: Char = 1012.toChar //GREEK CAPITAL THETA SYMBOL

    printf("%s %d%n", char977, char977.toInt)
    printf("%s %d%n", char1012, char1012.toInt)
    assert(char977 == 'ϑ')
    assert(char977.toInt == 977)
    assert(char1012 == 'ϴ')
    assert(char1012.toInt == 1012)
    println("toUpperCase")
    val char977UpperCase: Char = Character.toUpperCase(char977)
    val char1012UpperCase: Char = Character.toUpperCase(char1012)
    printf("%s %d%n", char977UpperCase, char977UpperCase.toInt)
    printf("%s %d%n", char1012UpperCase, char1012UpperCase.toInt)
    assert(char977UpperCase == 'Θ')
    assert(char977UpperCase.toInt == 920) //GREEK CAPITAL LETTER THETA
    assert(char1012UpperCase == 'ϴ')
    assert(char1012UpperCase.toInt == 1012)
    println("toLowerCase")
    val char977LowerCase: Char = Character.toLowerCase(char977)
    val char1012LowerCase: Char = Character.toLowerCase(char1012)
    printf("%s %d%n", char977LowerCase, char977LowerCase.toInt)
    printf("%s %d%n", char1012LowerCase, char1012LowerCase.toInt)
    assert(char977LowerCase == 'ϑ')
    assert(char977LowerCase.toInt == 977)
    assert(char1012LowerCase == 'θ')
    assert(char1012LowerCase.toInt == 952) //GREEK SMALL LETTER THETA
    println("toUpperCase.toLowerCase")
    val char977UpperCaseLowerCase: Char = Character.toLowerCase(char977UpperCase)
    val char1012UpperCaseLowerCase: Char = Character.toLowerCase(char1012UpperCase)
    printf("%s %d%n", char977UpperCaseLowerCase, char977UpperCaseLowerCase.toInt)
    printf("%s %d%n", char1012UpperCaseLowerCase, char1012UpperCaseLowerCase.toInt)
    assert(char977UpperCaseLowerCase == 'θ')
    assert(char977UpperCaseLowerCase.toInt == 952)
    assert(char1012UpperCaseLowerCase == 'θ')
    assert(char1012UpperCaseLowerCase.toInt == 952)
    println("toLowerCase.toUpperCase")
    val char977LowerCaseUpperCase: Char = Character.toUpperCase(char977LowerCase)
    val char1012LowerCaseUpperCase: Char = Character.toUpperCase(char1012LowerCase)
    printf("%s %d%n", char977LowerCaseUpperCase, char977LowerCaseUpperCase.toInt)
    printf("%s %d%n", char1012LowerCaseUpperCase, char1012LowerCaseUpperCase.toInt)
    assert(char977LowerCaseUpperCase == 'Θ')
    assert(char977LowerCaseUpperCase.toInt == 920)
    assert(char1012LowerCaseUpperCase == 'Θ')
    assert(char1012LowerCaseUpperCase.toInt == 920)
    println("Normalize with the NFKC form")
    val char977Normalized: Char = Normalizer.normalize(char977.toString, Normalizer.Form.NFKC).head
    val char1012Normalized: Char = Normalizer.normalize(char1012.toString, Normalizer.Form.NFKC).head
    printf("%s %d%n", char977Normalized, char977Normalized.toInt)
    printf("%s %d%n", char1012Normalized, char1012Normalized.toInt)
    assert(char977Normalized == 'θ')
    assert(char977Normalized.toInt == 952)
    assert(char1012Normalized == 'Θ')
    assert(char1012Normalized.toInt == 920)
    println("Normalize with the NFKC form toUpperCase")
    val char977NormalizedUpperCase: Char = Character.toUpperCase(char977Normalized)
    val char1012NormalizedUpperCase: Char = Character.toUpperCase(char1012Normalized)
    printf("%s %d%n", char977NormalizedUpperCase, char977NormalizedUpperCase.toInt)
    printf("%s %d%n", char1012NormalizedUpperCase, char1012NormalizedUpperCase.toInt)
    assert(char977NormalizedUpperCase == 'Θ')
    assert(char977NormalizedUpperCase.toInt == 920)
    assert(char1012NormalizedUpperCase == 'Θ')
    assert(char1012NormalizedUpperCase.toInt == 920)
    println("toUpperCase Normalize with the NFKC form")
    val char977UpperCaseNormalized: Char = Normalizer.normalize(char977UpperCase.toString, Normalizer.Form.NFKC).head
    val char1012UpperCaseNormalized: Char = Normalizer.normalize(char1012UpperCase.toString, Normalizer.Form.NFKC).head
    printf("%s %d%n", char977UpperCaseNormalized, char977UpperCaseNormalized.toInt)
    printf("%s %d%n", char1012UpperCaseNormalized, char1012UpperCaseNormalized.toInt)
    assert(char977UpperCaseNormalized == 'Θ')
    assert(char977UpperCaseNormalized.toInt == 920)
    assert(char1012UpperCaseNormalized == 'Θ')
    assert(char1012UpperCaseNormalized.toInt == 920)
    println("Normalize with the NFKC form toLowerCase")
    val char977NormalizedLowerCase: Char = Character.toLowerCase(char977Normalized)
    val char1012NormalizedLowerCase: Char = Character.toLowerCase(char1012Normalized)
    printf("%s %d%n", char977NormalizedLowerCase, char977NormalizedLowerCase.toInt)
    printf("%s %d%n", char1012NormalizedLowerCase, char1012NormalizedLowerCase.toInt)
    assert(char977NormalizedLowerCase == 'θ')
    assert(char977NormalizedLowerCase.toInt == 952)
    assert(char1012NormalizedLowerCase == 'θ')
    assert(char1012NormalizedLowerCase.toInt == 952)
    println("toLowerCase Normalize with the NFKC form")
    val char977LowerCaseNormalized: Char = Normalizer.normalize(char977LowerCase.toString, Normalizer.Form.NFKC).head
    val char1012LowerCaseNormalized: Char = Normalizer.normalize(char1012LowerCase.toString, Normalizer.Form.NFKC).head
    printf("%s %d%n", char977LowerCaseNormalized, char977LowerCaseNormalized.toInt)
    printf("%s %d%n", char1012LowerCaseNormalized, char1012LowerCaseNormalized.toInt)
    assert(char977LowerCaseNormalized == 'θ')
    assert(char977LowerCaseNormalized.toInt == 952)
    assert(char1012LowerCaseNormalized == 'θ')
    assert(char1012LowerCaseNormalized.toInt == 952)
    println("---")

    assert(char977 != char1012)
    //toUpperCase
    assert(Character.toUpperCase(char977) != Character.toUpperCase(char1012))
    //toLowerCase
    assert(Character.toLowerCase(char977) != Character.toLowerCase(char1012))
    //toUpperCase.toLowerCase
    assert(Character.toLowerCase(Character.toUpperCase(char977)) == Character.toLowerCase(Character.toUpperCase(char1012)))
    //toLowerCase.toUpperCase
    assert(Character.toUpperCase(Character.toLowerCase(char977)) == Character.toUpperCase(Character.toLowerCase(char1012)))
    //normalize with the NFKC form
    assert(Normalizer.normalize(char977.toString, Normalizer.Form.NFKC).head != Normalizer.normalize(char1012.toString, Normalizer.Form.NFKC).head)
    //normalize with the NFKC form toUpperCase
    assert(Character.toUpperCase(Normalizer.normalize(char977.toString, Normalizer.Form.NFKC).head) == Character.toUpperCase(Normalizer.normalize(char1012.toString, Normalizer.Form.NFKC).head))
    //toUpperCase normalize with the NFKC form
    assert(Normalizer.normalize(Character.toUpperCase(char977).toString, Normalizer.Form.NFKC) == Normalizer.normalize(Character.toUpperCase(char1012).toString, Normalizer.Form.NFKC))
    //normalize with the NFKC form toLowerCase
    assert(Normalizer.normalize(char977.toString, Normalizer.Form.NFKC).toLowerCase == Normalizer.normalize(char1012.toString, Normalizer.Form.NFKC).toLowerCase)
    //toLowerCase normalize with the NFKC form
    assert(Normalizer.normalize(Character.toLowerCase(char977).toString, Normalizer.Form.NFKC) == Normalizer.normalize(Character.toLowerCase(char1012).toString, Normalizer.Form.NFKC))
  }
```
***
<h3>コラム：配列と連結リスト</h3>
解説はWikipediaの記事「<a href="https://ja.wikipedia.org/wiki/%E9%80%A3%E7%B5%90%E3%83%AA%E3%82%B9%E3%83%88#.E9.80.A3.E7.B5.90.E3.83.AA.E3.82.B9.E3.83.88.E3.81.A8.E9.85.8D.E5.88.97" target="_blank">連結リストと配列</a>」を読んでください。<br>
実際の実装に関しては次を読んでください。
<ul>
<li><a href="http://www.ne.jp/asahi/hishidama/home/tech/scala/collection/index.html" target="_blank">Scalaコレクション</a></li>
<li><a href="http://www.ne.jp/asahi/hishidama/home/tech/scala/collection/seq.html" target="_blank">Scala Seq</a></li>
<li><a href="http://www.ne.jp/asahi/hishidama/home/tech/scala/collection/list.html" target="_blank">Scala List</a></li>
<li><a href="http://www.ne.jp/asahi/hishidama/home/tech/scala/collection/map.html" target="_blank">Scala Map</a></li>
<li><a href="http://www.ne.jp/asahi/hishidama/home/tech/java/collection.html" target="_blank">Java コレクションクラス</a></li>
</ul>
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
