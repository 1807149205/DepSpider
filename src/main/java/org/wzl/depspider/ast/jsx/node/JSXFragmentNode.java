package org.wzl.depspider.ast.jsx.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wzl.depspider.ast.core.node.ASTVisitor;

import java.util.List;

/**
 * JSX语法Fragment节点
 * 例如：<code> return <> <div>123</div> </> </code>
 * 这里的<></>就是一个Fragment节点
 *
 * @author weizhilong
 */
@Getter
@AllArgsConstructor
public class JSXFragmentNode extends JSXASTNode {

    private final List<JSXASTNode> children;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
