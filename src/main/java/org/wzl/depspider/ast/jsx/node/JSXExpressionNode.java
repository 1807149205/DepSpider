package org.wzl.depspider.ast.jsx.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wzl.depspider.ast.core.node.ASTVisitor;

/**
 * JSX语法表达式节点
 * 例如：<div>{ 1 + 2 }</div>
 * {1+2}就是一个表达式节点
 *
 * @author weizhilong
 */
@Getter
@AllArgsConstructor
public class JSXExpressionNode extends JSXASTNode{

    private final JSXASTNode expression;
    private final String rawExpression;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
