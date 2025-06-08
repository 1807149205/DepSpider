package org.wzl.depspider.ast.jsx.parser.node.definition.declaration;

import org.wzl.depspider.ast.jsx.parser.node.NodeType;
import org.wzl.depspider.ast.jsx.parser.node.definition.Loc;
import org.wzl.depspider.ast.jsx.parser.node.definition.Node;

public abstract class Declarator extends Node {


    public Declarator(NodeType nodeType, int start, int end, Loc loc) {
        super(nodeType, start, end, loc);
    }

}
