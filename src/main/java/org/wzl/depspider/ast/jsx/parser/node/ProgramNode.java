package org.wzl.depspider.ast.jsx.parser.node;

import lombok.Getter;
import lombok.Setter;
import org.wzl.depspider.ast.jsx.parser.enumerate.NodeType;
import org.wzl.depspider.ast.jsx.parser.enumerate.SourceType;
import org.wzl.depspider.ast.jsx.parser.node.definition.Extra;
import org.wzl.depspider.ast.jsx.parser.node.definition.Loc;
import org.wzl.depspider.ast.jsx.parser.node.definition.Node;

import java.util.List;

@Setter
@Getter
public class ProgramNode extends Node {

    private SourceType sourceType;

    private Object interpreter;

    private List<?> body;

    private List<?> directives;

    private Extra extra;

    public ProgramNode(int start, int end, Loc loc) {
        super(start, end, loc);
    }

}
