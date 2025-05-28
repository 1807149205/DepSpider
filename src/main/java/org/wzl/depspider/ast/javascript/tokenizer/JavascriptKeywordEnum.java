package org.wzl.depspider.ast.javascript.tokenizer;

import lombok.Getter;
import org.wzl.depspider.ast.core.tokenizer.TokenType;

import java.util.HashMap;
import java.util.Map;

/**
 * JavaScript 保留关键字枚举
 */
@Getter
public enum JavascriptKeywordEnum implements TokenType {
    BREAK("break"),
    CASE("case"),
    CATCH("catch"),
    CONTINUE("continue"),
    DEBUGGER("debugger"),
    DEFAULT("default"),
    DELETE("delete"),
    DO("do"),
    ELSE("else"),
    FINALLY("finally"),
    FOR("for"),
    FUNCTION("function"),
    IF("if"),
    IN("in"),
    INSTANCEOF("instanceof"),
    NEW("new"),
    RETURN("return"),
    SWITCH("switch"),
    THIS("this"),
    THROW("throw"),
    TRY("try"),
    TYPEOF("typeof"),
    VAR("var"),
    VOID("void"),
    WHILE("while"),
    WITH("with"),
    CLASS("class"),
    CONST("const"),
    ENUM("enum"),
    EXPORT("export"),
    EXTENDS("extends"),
    IMPORT("import"),
    SUPER("super"),
    YIELD("yield"),
    AWAIT("await"),
    LET("let"),
    STATIC("static");

    private final String keyword;

    JavascriptKeywordEnum(String keyword) {
        this.keyword = keyword;
    }

    // === 反查表：用于快速判断一个字符串是否是关键字 ===

    private static final Map<String, JavascriptKeywordEnum> keywordMap = new HashMap<>();

    static {
        for (JavascriptKeywordEnum kw : values()) {
            keywordMap.put(kw.keyword, kw);
        }
    }

    public static JavascriptKeywordEnum from(String word) {
        return keywordMap.get(word);
    }

    public static boolean isKeyword(String word) {
        return keywordMap.containsKey(word);
    }
}
