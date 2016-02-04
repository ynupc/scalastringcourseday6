# 1.　正規表現（regular expression、regex）

例：UCS-2を表す正規表現<strong>（ScalaのChar / Javaのcharに相当）</strong>
```
[\\x00-\\xFF][\\x00-\\xFF]
```

例：UTF-16を表す正規表現（ビッグエンディアン）

BOM（ビッグエンディアン）：
```
[\\xFE\\xFF]
```

文字：<strong>（Scala/Javaの文字）</strong>

```
(
               [\\x00-\\xD7\\xE0-\\xFF][\\x00-\\xFF]|//UCS-2
[\\xD8-\\xDB][\\x00-\\xFF][\\xDC-\\xDF][\\x00-\\xFF] //UTF-16代理領域（サロゲートペア）
//（左の2オクテットがhigh surrogate、右の2オクテットがlow surrogate）
)
```

例：UTF-16を表す正規表現（リトルエンディアン）

BOM（リトルエンディアン）：
```
[\\xFF\\xFE]
```

文字：
```
(
               [\\x00-\\xFF][\\x00-\\xD7\\xE0-\\xFF]|//UCS-2
[\\x00-\\xFF][\\xD8-\\xDB][\\x00-\\xFF][\\xDC-\\xDF] //UTF-16代理領域（サロゲートペア）
//（右の2オクテットがlow surrogate、左の2オクテットがhigh surrogate）
)
```

例：EUC-JPを表す正規表現
```
(
                  [\\x00-\\x7F]|//コードセット０ (ASCII/JIS ローマ字)
     [\\xA1-\\xFE][\\xA1-\\xFE]|//コードセット１（JIS X 0208:1997）
             \\x8E[\\xA0-\\xDF]|//コードセット２（半角片仮名）
\\x8F[\\xA1-\\xFE][\\xA1-\\xFE] //コードセット３（JIS X 0212-1990）
)
```
例：Shift-JISを表す正規表現
```
(
                                   [\\x00-\\x7F]|//ASCII/JIS ローマ字
[\\x81-\\x9F\\xE0-\\xFC][\\x40-\\x7E\\x80-\\xFC]|//JIS X 0208:1997
                                   [\\xA0-\\xDF] //半角片仮名
)
```
***
<h3>1.1　一致</h3>
<ul>
  <li>完全一致（exact match）：ABCDはABCDに完全一致</li>
  <li>部分一致（broad match / partial match）：BCはABCDに部分一致、下記の前方一致・後方一致は部分一致の特殊例、一般的に完全一致は部分一致に含めないが特殊例として解釈することも可能。
　  <ul>
　    <li>前方一致（forward match）：ABはABCDに前方一致</li>
　    <li>後方一致（backward match）：CDはABCDに後方一致</li>
　  </ul>
　</li>
</ul>
***
<h3>1.2　分割</h3>
区切り文字（デリミタ、delimiter）でトークン（token）に分割（split）する。
よくCSV、TSV、SSVファイルや統語解析器の出力結果をパースするときに使用します。<a href="https://github.com/ynupc/scalastringcourseday5/blob/master/doc/mutability.md" target="_blank">Day 5</a>で紹介したStringJoinerやString.joinメソッドでトークンをデリミタで結合するのと逆の処理になります。
StringTokenizer
String.split
Pattern.split
***
<h3>1.3　抽出</h3>
テキストからパターンマッチにより部分的なテキストを抽出するためにグループが使われます。<br>
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
***
<h3>コラム：チョムスキー階層</h3>
