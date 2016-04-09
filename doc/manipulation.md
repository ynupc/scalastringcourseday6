# 2.　文字列操作
<h3>2.1　比較</h3>
<h4>2.1.1　等値</h4>
<h4>2.1.2　パターンマッチ</h4>
<h4>2.1.3　文字列間の類似度</h4>
<h4>2.1.4　辞書順（Char順）</h4>
<h3>2.2　パスフィルタ</h3>
<h4>2.2.1　N番目の文字の取得</h4>
<h4>2.2.2　部分文字列の取得</h4>
<h4>2.2.3　先頭の一文字の取得</h4>
<h4>2.2.4　先頭のN文字の取得</h4>
<h4>2.2.5　末尾の一文字の取得</h4>
<h4>2.2.6　末尾のN文字の取得</h4>
<h4>2.2.7　条件式を満たす文字や文字列の取得</h4>
<h3>2.3　カットフィルタ</h3>
<h4>2.3.1　N番目の文字の削除</h4>
<h4>2.3.2　部分文字列の削除</h4>
<h4>2.3.3　先頭の一文字の削除</h4>
<h4>2.3.4　先頭のN文字の削除</h4>
<h4>2.3.5　先頭の文字列が一致したら削除</h4>
<h4>2.3.6　末尾の一文字の削除</h4>
<h4>2.3.7　末尾のN文字の削除</h4>
<h4>2.3.8　末尾の文字列が一致したら削除</h4>
<h4>2.3.9　末尾改行文字削除</h4>
<h4>2.3.10　前後の空白文字削除</h4>
<h4>2.3.11　条件式を満たす文字や文字列の削除</h4>
<h3>2.4　ソート</h3>
<h4>2.4.1　文字によるソート</h4>
<h4>2.4.2　逆順</h4>
<h3>2.5　集合演算</h3>
<h4>2.5.1　max</h4>
<h4>2.5.2　min</h4>
<h4>2.5.3　sum</h4>
<h4>2.5.4　product</h4>
<h4>2.5.5　union</h4>
<h4>2.5.6　diff</h4>
<h4>2.5.7　distinct</h4>
<h4>2.5.8　intersect</h4>
<h3>2.6　インデックス</h3>
<h3>2.7　イテレータ</h3>
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
<tr><td>頻度ベクトル</td><td></td></tr>
<tr><td>二値ベクトル</td><td></td></tr>
</table>
<h4>2.8.5　ベクトル間の類似度（自作）</h4>
<h4>2.8.6　ベクトル間の距離（自作）</h4>
<h4>2.8.7　ベクトル間の包含度（自作）</h4>
<h3>コラム：N-gramの読み方</h3>
