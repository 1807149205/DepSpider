package org.wzl.depspider.ast.jsx.parser.node.definition;

import org.wzl.depspider.ast.core.node.ASTNode;

import java.util.List;

public class ProgramNode {
    public String type;
    public int start;
    public int end;
    public Loc loc;
    public String sourceType;
    public Object interpreter; // 通常是 null
    public List<ASTNode> body;
}
