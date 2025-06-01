package org.wzl.depspider.ast.jsx.parser.node.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Identifier implements Expression {
    private String type;
    private int start;
    private int end;
    private Loc loc;
    private String name;

    @Override
    public String getType() {
        return type;
    }
}
