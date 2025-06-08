package org.wzl.depspider.ast.jsx.parser.node.definition;

import lombok.Getter;
import lombok.Setter;
import org.wzl.depspider.ast.jsx.parser.node.JSXNodeVisitor;
import org.wzl.depspider.ast.jsx.parser.node.NodeType;

import java.util.List;

@Getter
@Setter
public class ObjectExpression extends Expression {

    private List<ObjectProperty> properties;

    private Extra extra;

    public ObjectExpression(int start, int end, Loc loc) {
        super(NodeType.OBJECT_EXPRESSION, start, end, loc);
    }

    @Override
    public <T> T accept(JSXNodeVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
