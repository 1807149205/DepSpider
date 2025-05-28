package org.wzl.depspider.ast.jsx.parser.node.definition;

public class ExpressionStatement implements Statement {
    public String type;
    public int start;
    public int end;
    public Loc loc;
    public Expression expression;

    @Override
    public String getType() {
        return this.type;
    }
}
