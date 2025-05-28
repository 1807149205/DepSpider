package org.wzl.depspider.ast.jsx.parser.node.definition;

import java.util.List;

public class BlockStatement implements Expression {
    public String type;
    public int start;
    public int end;
    public Loc loc;
    public List<Statement> body;
    public List<Object> directives;

    @Override
    public String getType() {
        return type;
    }
}
