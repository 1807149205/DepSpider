package org.wzl.depspider.ast.jsx.parser.node.definition;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StringLiteral {
    private String type;
    private int start;
    private int end;
    private Loc loc;
    private Extra extra;
    private String value;

    @Override
    public String toString() {
        return "StringLiteral{ name : " + value + " }";
    }
}