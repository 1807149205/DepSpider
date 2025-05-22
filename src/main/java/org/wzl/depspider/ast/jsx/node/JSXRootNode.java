package org.wzl.depspider.ast.jsx.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wzl.depspider.ast.core.node.ASTVisitor;

import java.util.List;

@Getter
@AllArgsConstructor
public class JSXRootNode extends JSXASTNode {

    private final List<JSXASTNode> children;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
