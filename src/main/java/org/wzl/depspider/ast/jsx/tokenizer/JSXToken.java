package org.wzl.depspider.ast.jsx.tokenizer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.wzl.depspider.ast.core.tokenizer.Token;
import org.wzl.depspider.ast.core.tokenizer.TokenType;

/**
 * jsx的token
 *
 * @author weizhilong
 */
@Getter
@ToString
@AllArgsConstructor
public class JSXToken implements Token {
    public enum Type implements TokenType{
        // JavaScript 基础
        KEYWORD,
        IDENTIFIER,
        NUMBER,
        STRING,
        OPERATOR,
        OPERATOR_OR_JSX_TAG_START,
        PUNCTUATION,
        WHITESPACE,
        COMMENT,
        LEFT_PARENTHESIS,              // (
        RIGHT_PARENTHESIS,             // )

        // JSX 专用
        JSX_TEXT,                 // 文本节点
        JSX_TAG_START,           // <
        JSX_TAG_END,             // >
        JSX_TAG_SELF_CLOSE,      // />
        JSX_TAG_CLOSE,           // </
        JSX_IDENTIFIER,          // div, span, MyComponent
        JSX_ATTR_NAME,           // 属性名
        JSX_ATTR_VALUE_STRING,   // 属性值（字符串）
        JSX_ATTR_VALUE_EXPR,     // 属性值（{表达式}）
        JSX_EXPR_START,          // {
        JSX_EXPR_END,            // }
        JSX_FRAGMENT_START,      // <>
        JSX_FRAGMENT_END,        // </>

        // 文件末尾
        EOF
    }

    private final Type type;
    private final String value;
    private final int start;
    private final int end;
    private final int line;

}
