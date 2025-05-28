package org.wzl.depspider.ast.jsx.tokenizer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wzl.depspider.ast.core.tokenizer.Token;
import org.wzl.depspider.ast.core.tokenizer.TokenType;

/**
 * jsx的token
 *
 * @author weizhilong
 */
@Getter
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

        // 文件末尾
        EOF
    }

    private final Type type;
    private final String value;


    @Override
    public TokenType getType() {
        return this.type;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public int getStart() {
        return 0;
    }

    @Override
    public int getEnd() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Token(%s, \"%s\")", type, value);
    }
}
