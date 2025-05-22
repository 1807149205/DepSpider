package org.wzl.depspider.ast.jsx.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wzl.depspider.ast.core.node.ASTNode;
import org.wzl.depspider.ast.core.node.ASTVisitor;

import java.util.List;

/**
 * JSX语言的元素节点
 * <div /> 或 <Button prop="value" />	JSXElementNode
 * <div prop="value">aa</div>
 *
 * @author weizhilong
 */
@Getter
@AllArgsConstructor
public class JSXElementNode extends JSXASTNode {

    private final String tagName;
    private final List<JSXAttributeNode> attributes;
    private final List<ASTNode> children;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
