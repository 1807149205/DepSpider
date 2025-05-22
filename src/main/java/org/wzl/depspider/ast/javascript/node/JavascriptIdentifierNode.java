package org.wzl.depspider.ast.javascript.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wzl.depspider.ast.core.node.ASTVisitor;

/**
 * javascript变量名，可以用作函数名、参数名、属性名等等
 * 例如：
 */
@Getter
@AllArgsConstructor
public class JavascriptIdentifierNode extends JavascriptASTNode {

    private final String identifier;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
