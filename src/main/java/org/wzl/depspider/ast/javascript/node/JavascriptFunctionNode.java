package org.wzl.depspider.ast.javascript.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wzl.depspider.ast.core.node.ASTVisitor;

/**
 * JavaScript函数节点，包含：
 * <ul>
 *   <li>箭头函数：<code>const a = () =&gt; { }</code></li>
 *   <li>普通函数：<code>function a() { }</code></li>
 *   <li>匿名函数：<code>() =&gt; { }</code></li>
 *   <li>函数表达式：<code>const a = function() { }</code></li>
 * </ul>
 */

@Getter
@AllArgsConstructor
public class JavascriptFunctionNode extends JavascriptASTNode {

    public enum FunctionType {
        ARROW_FUNCTION,         // 箭头函数
        FUNCTION_DECLARATION,   // 函数声明
        FUNCTION_EXPRESSION,    // 函数表达式
        ANONYMOUS_FUNCTION      // 匿名函数
    }

    private final FunctionType functionType;
    private final JavascriptIdentifierNode identifier;
    private final JavascriptASTNode body;
    private final JavascriptASTNode parameters;
    private final JavascriptASTNode returnType;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
