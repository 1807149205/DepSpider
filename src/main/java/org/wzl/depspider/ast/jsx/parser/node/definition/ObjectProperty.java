package org.wzl.depspider.ast.jsx.parser.node.definition;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.wzl.depspider.ast.jsx.parser.node.JSXNodeVisitor;
import org.wzl.depspider.ast.jsx.parser.node.NodeType;
import org.wzl.depspider.ast.jsx.parser.node.definition.literal.StringLiteral;

@Setter
@Getter
public class ObjectProperty extends Property {

    /**
     * 是否为方法
     * eg. const a = () => {}  return true
     */
    private boolean method;

    private Object key;

    private boolean computed;

    private boolean shorthand;

    private StringLiteral value;

    public ObjectProperty(int start, int end, Loc loc) {
        super(NodeType.OBJECT_PROPERTY, start, end, loc);
    }

    @Override
    public <T> T accept(JSXNodeVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
