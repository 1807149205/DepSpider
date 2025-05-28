package org.wzl.depspider.ast.jsx.parser.node.definition;

public class Identifier implements Expression {
    public String type;
    public int start;
    public int end;
    public Loc loc;
    public String name;

    @Override
    public String getType() {
        return type;
    }
}
