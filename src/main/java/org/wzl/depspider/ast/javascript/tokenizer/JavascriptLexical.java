package org.wzl.depspider.ast.javascript.tokenizer;

import lombok.extern.slf4j.Slf4j;
import org.wzl.depspider.ast.core.tokenizer.LexicalAnalysis;
import org.wzl.depspider.ast.core.tokenizer.Token;
import org.wzl.depspider.ast.core.tokenizer.TokenType;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Javascript语言的词法分析器
 *
 * @author weizhilong
 */
@Slf4j
public class JavascriptLexical<T extends Token> implements LexicalAnalysis<T> {

    @Override
    public List<T> analyze(String code) {
        return Collections.emptyList();
    }

    @Override
    public List<T> analyze(InputStream in) {
        return Collections.emptyList();
    }

    /**
     * 根据字符获取当前的Token类型
     * @param c 当前字符
     * @return  Token类型
     */
    protected TokenType getTokenType(char c) {
        switch (c) {
            // 操作符
            case '+': case '-': case '*': case '/':
            case '%': case '=': case '!': case '<':
            case '>': case '&': case '|': case '^':
            case '~': case '?': case ':':
                return JavaScriptTokenType.OPERATOR;

            // 分隔符
            case ',':
                return JavaScriptTokenType.COMMA;
            case ';':
                return JavaScriptTokenType.SEMICOLON;
            case '.':
                return JavaScriptTokenType.DOT;

            // 括号
            case '(':
                return JavaScriptTokenType.PAREN_OPEN;
            case ')':
                return JavaScriptTokenType.PAREN_CLOSE;
            case '{':
                return JavaScriptTokenType.BRACE_OPEN;
            case '}':
                return JavaScriptTokenType.BRACE_CLOSE;
            case '[':
                return JavaScriptTokenType.BRACKET_OPEN;
            case ']':
                return JavaScriptTokenType.BRACKET_CLOSE;

            default:
                return JavaScriptTokenType.UNKNOWN;
        }
    }

    /**
     * 读取标识符或关键字
     * @param code      代码文件
     * @param index     当前下标
     * @return          读取到的标识符或关键字
     */
    protected String readIdentifierOrKeyword(String code, int index) {
        StringBuilder sb = new StringBuilder();
        while (index < code.length()) {
            char c = code.charAt(index);
            if (Character.isLetter(c) || c == '_') {
                sb.append(c);
            } else {
                break;
            }
            index++;
        }
        return sb.toString();
    }
}

