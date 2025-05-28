package org.wzl.depspider.ast.jsx.tokenizer;

import org.wzl.depspider.ast.core.tokenizer.Token;
import org.wzl.depspider.ast.core.tokenizer.Tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * jsx词法解析器
 *
 * @author weizhilong
 */
public class JSXTokenizer implements Tokenizer {

    private static final Set<String> KEYWORDS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    "var", "let", "const", "function", "if", "else", "return", "for", "while"
            ))
    );


    private String input;
    private int pos = 0;

    public Token peek() {
        return null;
    }

    /**
     * 获取当前字符
     * @return  当前字符，如果到达输入末尾则返回'\0'
     */
    public char peekChar() {
        return pos < input.length() ? input.charAt(pos) : '\0';
    }

    @Override
    public Token next() {
        return null;
    }

    /**
     * 获取输入字符串中的下一个字符
     * 如果到达输入末尾则返回'\0'
     */
    private char advance() {
        return pos < input.length() ? input.charAt(pos++) : '\0';
    }

    @Override
    public boolean isAtEnd() {
        return pos >= input.length();
    }

    private void skipWhitespace() {
        while (!isAtEnd() && Character.isWhitespace(peekChar())) {
            advance();
        }
    }

    private char getNextChar() {
        if (isAtEnd()) {
            return '\0'; // 如果到达输入末尾，返回'\0'
        }
        return input.charAt(pos + 1); // 返回下一个字符
    }


    @Override
    public void setSource(String source) {
        this.input = source;
    }

    public List<Token> tokenize() {
        if (input == null) {
            throw new IllegalStateException("No source code provided.");
        }

        List<Token> tokens = new ArrayList<>();

        while (!isAtEnd()) {
            skipWhitespace();
            char c = peekChar();

            if (Character.isLetter(c) || c == '_') {    //变量或者关键词
                tokens.add(readIdentifierOrKeyword());
            } else if (c == '>' || c == '<') {          // JSX标签或运算符
                tokens.add(readJSXOrOperator());
            } else if (Character.isDigit(c)) {          //数字
                tokens.add(readNumber());
            } else if (c == '"' || c == '\'') {         //字符串
                tokens.add(readString());
            } else if (c == '/') {                      //注释
                tokens.add(readCommentOrJSX());
            } else if (c == '{') {
                tokens.add(new JSXToken(JSXToken.Type.JSX_EXPR_START, advance() + ""));
            } else if (c == '}') {
                tokens.add(new JSXToken(JSXToken.Type.JSX_EXPR_END, advance() + ""));
            } else if (c == '[') {
                tokens.add(new JSXToken(JSXToken.Type.JSX_TAG_START, advance() + ""));
            } else if (c == ']') {
                tokens.add(new JSXToken(JSXToken.Type.JSX_TAG_END, advance() + ""));
            } else if (c == ')') {
                tokens.add(new JSXToken(JSXToken.Type.JSX_TAG_SELF_CLOSE, advance() + ""));
            } else if (c == '(') {
                tokens.add(new JSXToken(JSXToken.Type.JSX_TAG_CLOSE, advance() + ""));
            } else if (c == '=' || c == '!' || c == '+' || c == '-' || c == '*' || c == '%') {
                tokens.add(new JSXToken(JSXToken.Type.OPERATOR, advance() + ""));
            } else {
                advance();
            }
        }

        tokens.add(new JSXToken(JSXToken.Type.EOF, ""));
        return tokens;
    }

    /**
     * 读取注释或JSX标签或运算符
     * 两种可能：
     *      1. /> JSX标签结束符
     *      2. // 注释开始
     */
    private Token readCommentOrJSX() {
        char nextPosChar = getNextChar();
        if (peekChar() == '/' && nextPosChar == '>') {
            return new JSXToken(JSXToken.Type.JSX_TAG_SELF_CLOSE, advance() + "");
        }
        return readComment();
    }


    /**
     * 读取JSX标签或运算符
     */
    private Token readJSXOrOperator() {
        char nextPosChar = getNextChar();
        if (peekChar() == '<' && nextPosChar == '/') {
            JSXToken jsxToken = new JSXToken(JSXToken.Type.JSX_TAG_CLOSE, advance() + "/");
            advance();
            return jsxToken;
        }
        return new JSXToken(JSXToken.Type.OPERATOR_OR_JSX_TAG_START, advance() + "");
    }

    /**
     * 读取注释
     * 单行注释： // wqeqw
     * 多行注释： \/* wqeqw *\/
     * 块注释： \/** wqeqw \*\/
     */
    private Token readComment() {
        advance();
        char nextChar = advance();

        Token commentToken = null;
        StringBuilder sb = new StringBuilder();

        if (nextChar == '/') {
            while (!isAtEnd() && peekChar() != '\n') {
                sb.append(advance());
            }
        } else if (nextChar == '*') {
            while (!isAtEnd()) {
                char peekChar = advance();  //last char
                if (peekChar == '*' && peekChar() == '/') {
                    advance(); advance();
                    break;
                }
                sb.append(peekChar);
            }
        }
        return new JSXToken(JSXToken.Type.COMMENT, sb.toString());
    }

    private Token readIdentifierOrKeyword() {
        int start = pos;
        while (!isAtEnd() && (Character.isLetterOrDigit(peekChar()) || peekChar() == '_')) {
            advance();
        }
        String word = input.substring(start, pos);
        if (KEYWORDS.contains(word)) {
            return new JSXToken(JSXToken.Type.KEYWORD, word);
        }
        return new JSXToken(JSXToken.Type.IDENTIFIER, word);
    }

    private Token readNumber() {
        int start = pos;
        while (!isAtEnd() && Character.isDigit(peekChar())) {
            advance();
        }
        return new JSXToken(JSXToken.Type.NUMBER, input.substring(start, pos));
    }

    private Token readString() {
        char quote = advance(); // consume opening quote
        int start = pos;
        while (!isAtEnd() && peekChar() != quote) {
            advance();
        }
        String value = input.substring(start, pos);
        advance(); // consume closing quote
        return new JSXToken(JSXToken.Type.STRING, value);
    }
}
