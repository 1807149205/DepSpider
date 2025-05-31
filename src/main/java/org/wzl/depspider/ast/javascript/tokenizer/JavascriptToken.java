package org.wzl.depspider.ast.javascript.tokenizer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wzl.depspider.ast.core.tokenizer.Token;

@Getter
@AllArgsConstructor
public class JavascriptToken implements Token {

    private final int start;
    private final int end;
    private final JavaScriptTokenType type;
    private final String value;
    private final int line;
}
