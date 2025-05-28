package org.wzl.depspider.ast.jsx.parser.node.definition;

import lombok.Data;
import org.wzl.depspider.ast.jsx.parser.enumerate.NodeType;

@Data
public class Node {

    private int start;

    private int end;

    private Loc loc;

    public Node(int start, int end, Loc loc) {
        this.start = start;
        this.end = end;
        this.loc = loc;
    }

}
