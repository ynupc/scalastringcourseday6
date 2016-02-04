# 1.　正規表現（regular expression、regex）
<h3>1.1　一致（match）</h3>
<ul>
  <li>完全一致（exact match）：ABCDはABCDに完全一致</li>
  <li>部分一致（broad match / partial match）：BCはABCDに部分一致、下記の前方一致・後方一致は部分一致の特殊例、一般的に完全一致は部分一致に含めないが特殊例として解釈することも可能。
　  <ul>
　    <li>前方一致（forward match）：ABはABCDに前方一致</li>
　    <li>後方一致（backward match）：CDはABCDに後方一致</li>
　  </ul>
　</li>
</ul>
<h3>1.2　分割（split）</h3>
区切り文字（デリミタ、delimiter）でトークン（token）に分割（split）する。

StringTokenizer
String.split
Pattern.split
<h3>1.3　抽出（group）</h3>
<h3>1.4　置換（replace）</h3>
