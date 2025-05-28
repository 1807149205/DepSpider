package org.wzl.depspider.ast.javascript.tokenizer;

import org.wzl.depspider.ast.core.tokenizer.TokenType;

public enum JavaScriptTokenType implements TokenType {

    // 基础结构
    IDENTIFIER,          // 变量名、函数名
    KEYWORD,             // 保留关键字：function, return 等
    NUMBER_LITERAL,      // 数字字面量：123, 3.14
    STRING_LITERAL,      // 字符串："hello", 'world'
    BOOLEAN_LITERAL,     // true, false
    NULL_LITERAL,        // null
    UNDEFINED_LITERAL,   // undefined

    // 运算符
    OPERATOR,            // +, -, *, /, %, +=, ==, === 等
    LOGICAL_OPERATOR,    // &&, ||, !
    BITWISE_OPERATOR,    // &, |, ^, ~, <<, >>

    // 比较
    COMPARISON_OPERATOR, // ==, !=, ===, !==, >, >=, <, <=

    // 符号和分隔符
    COMMA,               // ,
    SEMICOLON,           // ;
    COLON,               // :
    DOT,                 // .
    QUESTION_MARK,       // ?
    SPREAD_OPERATOR,     // ...

    // 括号
    PAREN_OPEN,          // (
    PAREN_CLOSE,         // )
    BRACE_OPEN,          // {
    BRACE_CLOSE,         // }
    BRACKET_OPEN,        // [
    BRACKET_CLOSE,       // ]

    // 其他
    COMMENT,             // // line comment 或 /* block comment */
    TEMPLATE_LITERAL,    // `template string`
    REGEXP_LITERAL,      // /abc/i
    WHITESPACE,          // 空格、换行、Tab
    NEWLINE,             // \n, \r\n
    UNKNOWN,             // 无法识别的字符

    // EOF（结束标记）
    EOF;                 // End of File
}
