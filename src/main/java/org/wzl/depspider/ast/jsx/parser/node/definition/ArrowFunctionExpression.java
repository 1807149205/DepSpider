package org.wzl.depspider.ast.jsx.parser.node.definition;

import java.util.List;

public class ArrowFunctionExpression implements Expression {
    public String type;
    public int start;
    public int end;
    public Loc loc;
    public Object id; // null
    public boolean generator;
    public boolean async;
    public List<Identifier> params;
    public BlockStatement body;

    @Override
    public String getType() {
        return type;
    }
}
