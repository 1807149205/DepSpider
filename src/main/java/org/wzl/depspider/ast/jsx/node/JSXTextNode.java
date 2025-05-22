package org.wzl.depspider.ast.jsx.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wzl.depspider.ast.core.node.ASTVisitor;

/**
 * JSX文本
 * 例如：<div>123</div> , 123代表文本
 *
 * @author weizhilong
 */
@Getter
@AllArgsConstructor
public class JSXTextNode extends JSXASTNode{

    private final String text;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
