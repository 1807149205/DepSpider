package org.wzl.depspider.ast.javascript.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wzl.depspider.ast.core.node.ASTVisitor;

/**
 * JavaScript函数节点
 * 包含：
 *   1、箭头函数： const a = () => {   }
 *   2、普通函数： function a() {   }
 *   3、匿名函数： () => {   }
 *   4、函数表达式： const a = function() {   }
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
