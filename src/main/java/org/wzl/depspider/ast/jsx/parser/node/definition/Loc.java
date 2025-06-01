package org.wzl.depspider.ast.jsx.parser.node.definition;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Loc {
    public Position start;
    public Position end;
}

