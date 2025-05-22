package org.wzl.depspider.ast.jsx.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wzl.depspider.ast.core.node.ASTNode;
import org.wzl.depspider.ast.core.node.ASTVisitor;

/**
 * jsx语法中的标签参数
 * 	prop="value" 或 prop={expression}
 *
 * @author weizhilong
 */
@Getter
@AllArgsConstructor
public class JSXAttributeNode extends JSXASTNode {

    private final String name;
    /**
     * type: JSXTextNode | JSXExpressionNode
     */
    private final ASTNode value;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
