package org.wzl.depspider.ast.jsx.parser.node;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.wzl.depspider.ast.jsx.parser.enumerate.SourceType;
import org.wzl.depspider.ast.jsx.parser.node.definition.Extra;
import org.wzl.depspider.ast.jsx.parser.node.definition.Loc;
import org.wzl.depspider.ast.jsx.parser.node.definition.Node;

import java.util.List;

@Setter
@Getter
@ToString
public class ProgramNode extends Node {

    private SourceType sourceType;

    private Object interpreter;

    private List<Node> body;

    private List<Node> directives;

    private Extra extra;

    public ProgramNode(int start, int end, Loc loc) {
        super(NodeType.PROGRAM, start, end, loc);
    }

}
