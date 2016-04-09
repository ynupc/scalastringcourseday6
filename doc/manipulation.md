# 2.　文字列操作
<h3>2.1　比較</h3>
<h4>2.1.1　等値</h4>
<h4>2.1.2　辞書順（Char順）</h4>
<h3>2.2　パスフィルタ</h3>
<h4>N番目のChar</h4>
<h4>部分文字列</h4>
<h4>先頭の一文字</h4>
<h4>先頭のN文字</h4>
<h4>末尾の一文字</h4>
<h4>末尾のN文字</h4>
<h4>条件式を</h4>
<h3>2.3　カットフィルタ</h3>
<h3>2.4　ソート</h3>
<h3>2.5　集合演算</h3>
<h3>2.6　インデックス</h3>
<h3>2.7　イテレータ</h3>
<h3>2.8　文字列間の文字に注目した類似度</h3>
<h4>2.8.1　文字列間の文字に注目した比較単位</h4>
文字はCharではなくコードポイントで扱う方が確実です。Charは文字列にサロゲートペアを含む場合には文字を表しませんが、一般的な英語のテキストようにサロゲートペアが入らないことが保証されている場合は文字として扱うこともできます。
<table>
<tr><th>単位</th><th>説明</th></tr>
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
<tr>
 <td>文字LCS<br>（Longest Common Subsequence）</td>
 <td>二つの文字列を比較して共通する文字の集合。文字列の順序は考慮する。
 <br>例：
 </td>
</tr>
<tr>
 <td>文字WLCS<br>（Weighted Longest Common Subsequence）</td>
 <td>二つの文字列を比較して共通する文字の集合。文字列の順序は考慮する。
 <br>例：
 </td>
</tr>
</table>
<h4>2.8.2　レーベンシュタイン距離（自作）</h4>
<h4>2.8.3　LCS（自作）</h4>
<h4>2.8.4　ベクトル化（自作）</h4>
<table>
<tr><th>ベクトルの種類</th><th>説明</th></tr>
<tr><td>頻度ベクトル</td><td></td></tr>
<tr><td>二値ベクトル</td><td></td></tr>
</table>
<h4>2.8.5　類似度（自作）</h4>
<h4>2.8.6　包含度（自作）</h4>
