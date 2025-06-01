package org.wzl.depspider.ast.jsx.parser.node.definition;

import lombok.Data;
import org.wzl.depspider.ast.jsx.parser.node.NodeType;

@Data
public class Node {

    private NodeType nodeType;

    private int start;

    private int end;

    private Loc loc;

    public Node(NodeType nodeType, int start, int end, Loc loc) {
        this.nodeType = nodeType;
        this.start = start;
        this.end = end;
        this.loc = loc;
    }

}
