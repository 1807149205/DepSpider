package org.wzl.depspider.ast.jsx.parser.node.definition.literal;

import org.wzl.depspider.ast.jsx.parser.node.NodeType;
import org.wzl.depspider.ast.jsx.parser.node.definition.Expression;
import org.wzl.depspider.ast.jsx.parser.node.definition.Loc;

public abstract class Literal extends Expression {

    public Literal(NodeType nodeType, int start, int end, Loc loc) {
        super(nodeType, start, end, loc);
    }

}
