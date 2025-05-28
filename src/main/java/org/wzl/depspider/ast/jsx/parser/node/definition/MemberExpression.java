package org.wzl.depspider.ast.jsx.parser.node.definition;

public class MemberExpression implements Expression {
    public String type;
    public int start;
    public int end;
    public Loc loc;
    public Expression object;
    public boolean computed;
    public Identifier property;

    @Override
    public String getType() {
        return type;
    }
}
