package org.wzl.depspider.ast.jsx.parser.node.definition;

import org.wzl.depspider.ast.jsx.parser.node.NodeType;

public abstract class Property extends Node{

    public Property(NodeType nodeType, int start, int end, Loc loc) {
        super(nodeType, start, end, loc);
    }

}
