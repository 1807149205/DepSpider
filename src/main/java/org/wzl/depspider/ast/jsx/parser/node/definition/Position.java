package org.wzl.depspider.ast.jsx.parser.node.definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Position {
    private int line;
    private int column;
    private int index;
}