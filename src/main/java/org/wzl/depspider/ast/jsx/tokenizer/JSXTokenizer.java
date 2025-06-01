package org.wzl.depspider.ast.jsx.tokenizer;

import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class JSXTokenizer implements Tokenizer {

    private static final Set<String> KEYWORDS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    "var", "let", "const", "function", "if", "else", "return", "for", "while"
            ))
    );

    /**
     * 当前扫描代码文件的行数
     */
    private int line = 1;

    /**
     * 原始代码文件
     */
    private String input;

    /**
     * 当前所扫描代码文件的下标
     * 不能直接修改改坐标的值，可通过getLinePos(),advance()方法来调用
     */
    private int pos = 0;

    /**
     * 相对于行的下标
     */
    private int linePos = 0;

    /**
     * 获取当前文件扫描下标
     * @return  下标
     */
    private int getPos() {
        return this.pos;
    }

    /**
     * 获取输入字符串中的下一个字符
     * 如果到达输入末尾则返回'\0'
     */
    private char advance() {
        if (pos < input.length()) {
            char c = peekChar();
            linePos++;
            if (input.charAt(pos) == '\n') {
                line++;
                linePos = 0;
            }
            pos++;
            return c;
        }
        return '\0';
    }

    private int getLine() {
        return this.line;
    }

    private int getLinePos() {
        return this.linePos;
    }

    public Token peek() {
        return null;
    }

    /**
     * 获取当前字符
     * @return  当前字符，如果到达输入末尾则返回'\0'
     */
    private char peekChar() {
        return pos < input.length() ? input.charAt(pos) : '\0';
    }

    @Override
    public Token next() {
        return null;
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

    /**
     * 获取下一个字符
     */
    private char getNextChar() {
        if (isAtEnd()) {
            return '\0'; // 如果到达输入末尾，返回'\0'
        }
        return input.charAt(pos + 1); // 返回下一个字符
    }

    /**
     * 获取 pos+2的字符
     */
    private char getDoubleNextChar() {
        if (pos + 2 < input.length()) {
            return input.charAt(pos + 2);
        }
        return '\0';
    }


    @Override
    public void setSource(String source) {
        this.input = source;
    }

    @Override
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
                tokens.add(
                        new JSXToken(
                                JSXToken.Type.JSX_EXPR_START,
                                advance() + "",
                                getPos(),
                                getPos(),
                                getLine(),
                                getLinePos()
                        )
                );
            } else if (c == '}') {
                tokens.add(
                        new JSXToken(
                                JSXToken.Type.JSX_EXPR_END,
                                advance() + "",
                                getPos(),
                                getPos(),
                                getLine(),
                                getLinePos()
                        )
                );
            } else if (c == '[') {
                tokens.add(
                        new JSXToken(
                                JSXToken.Type.JSX_TAG_START,
                                advance() + "",
                                getPos(),
                                getPos(),
                                getLine(),
                                getLinePos()
                        )
                );
            } else if (c == ']') {
                tokens.add(
                        new JSXToken(
                                JSXToken.Type.JSX_TAG_END,
                                advance() + "",
                                getPos(),
                                getPos(),
                                getLine(),
                                getLinePos()
                        )
                );
            } else if (c == ')') {
                tokens.add(
                        new JSXToken(
                                JSXToken.Type.RIGHT_PARENTHESIS,
                                advance() + "",
                                getPos(),
                                getPos(),
                                getLine(),
                                getLinePos()
                        )
                );
            } else if (c == '(') {
                tokens.add(
                        new JSXToken(
                                JSXToken.Type.LEFT_PARENTHESIS,
                                advance() + "",
                                getPos(),
                                getPos(),
                                getLine(),
                                getLinePos()
                        )
                );
            } else if (c == '=' || c == '!' || c == '+' || c == '-' || c == '*' || c == '%') {
                tokens.add(
                        new JSXToken(
                                JSXToken.Type.OPERATOR,
                                advance() + "",
                                getPos(),
                                getPos(),
                                getLine(),
                                getLinePos()
                        )
                );
            } else {
                advance();
            }
        }

        tokens.add(
                new JSXToken(
                        JSXToken.Type.EOF,
                        "",
                        getPos(),
                        getPos(),
                        getLine(),
                        getLinePos()
                )
        );
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
            JSXToken jsxToken = new JSXToken(
                    JSXToken.Type.JSX_TAG_SELF_CLOSE,
                    "/>",
                    getPos(),
                    getPos() + 1,
                    getLine(),
                    getLinePos()
            );
            advance(); advance();   // pos += 2
            return jsxToken;
        }
        return readComment();
    }


    /**
     * 读取JSX标签或运算符
     */
    private Token readJSXOrOperator() {
        char nextPosChar = getNextChar();

        if (peekChar() == '<' && getNextChar() == '>') {
            JSXToken jsxToken = new JSXToken(
                    JSXToken.Type.JSX_FRAGMENT_START,
                    "<>",
                    getPos(),
                    getPos() + 1,
                    getLine(),
                    getLinePos()
            );
            advance(); advance();
            return jsxToken;
        }

        if (peekChar() == '<' && getNextChar() == '/' && getDoubleNextChar() == '>') {
            JSXToken jsxToken = new JSXToken(
                    JSXToken.Type.JSX_FRAGMENT_END,
                    "</>",
                    getPos(),
                    getPos() + 2,
                    getLine(),
                    getLinePos()
            );
            advance(); advance(); advance();
            return jsxToken;
        }

        if (peekChar() == '<' && nextPosChar == '/') {
            JSXToken jsxToken = new JSXToken(
                    JSXToken.Type.JSX_TAG_CLOSE,
                    "</",
                    getPos(),
                    getPos() + 1,
                    getLine(),
                    getLinePos()
            );
            advance(); advance(); // pos += 2;
            return jsxToken;
        }

        return new JSXToken(
                JSXToken.Type.OPERATOR_OR_JSX_TAG_START,
                advance() + "",
                getPos(),
                getPos() + 1,
                getLine(),
                getLinePos()
        );
    }

    /**
     * 读取注释
     * 单行注释： // wqeqw
     * 多行注释： \/* wqeqw *\/
     * 块注释： \/** wqeqw \*\/
     */
    private Token readComment() {
        int startPos = getPos();
        advance();
        char nextChar = advance();

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
        return new JSXToken(
                JSXToken.Type.COMMENT,
                sb.toString(),
                startPos,
                getPos(),
                getLine(),
                getLinePos()
        );
    }

    /**
     * 解析变量或关键字
     */
    private Token readIdentifierOrKeyword() {
        int start = getPos();
        while (!isAtEnd() && (Character.isLetterOrDigit(peekChar()) || peekChar() == '_')) {
            advance();
        }
        String word = input.substring(start, getPos());
        if (KEYWORDS.contains(word)) {
            return new JSXToken(
                    JSXToken.Type.KEYWORD,
                    word,
                    start,
                    getPos(),
                    getLine(),
                    getLinePos()
            );
        }
        return new JSXToken(
                JSXToken.Type.IDENTIFIER,
                word,
                start,
                getPos(),
                getLine(),
                getLinePos()
        );
    }

    /**
     * 解析数字
     */
    private Token readNumber() {
        int start = getPos();
        while (!isAtEnd() && Character.isDigit(peekChar())) {
            advance();
        }
        return new JSXToken(
                JSXToken.Type.NUMBER,
                input.substring(start, getPos()),
                start,
                getPos(),
                getLine(),
                getLinePos()
        );
    }

    /**
     * 解析字符串
     */
    private Token readString() {
        char quote = advance(); // consume opening quote
        int start = getPos();
        while (!isAtEnd() && peekChar() != quote) {
            advance();
        }
        String value = input.substring(start, getPos());
        advance(); // consume closing quote
        return new JSXToken(
                JSXToken.Type.STRING,
                value,
                start,
                getPos(),
                getLine(),
                getLinePos()
        );
    }
}
