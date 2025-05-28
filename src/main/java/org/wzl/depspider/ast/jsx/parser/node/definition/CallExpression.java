package org.wzl.depspider.ast.jsx.parser.node.definition;

import java.util.List;

public class CallExpression implements Expression {
    public String type;
    public int start;
    public int end;
    public Loc loc;
    public MemberExpression callee;
    public List<Expression> arguments;

    @Override
    public String getType() {
        return type;
    }
}
