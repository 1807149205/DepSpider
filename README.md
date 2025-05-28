# DepSpider
This framework is built with Java and is designed to scan and analyze page-level component dependencies within React-based front-end projects.


源码 → [词法分析] → Token → [语法分析] → AST → [优化/检查] → [解释/编译/分析/转译]

源文本->扫描器（Scanner）->Tokenizer->解析器（Parser）

5 + (1 x 12)  -> Tokenizer -> [5, +, (, 1, x, 12, )] -> Parser -> AST