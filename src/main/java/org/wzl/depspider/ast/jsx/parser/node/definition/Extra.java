package org.wzl.depspider.ast.jsx.parser.node.definition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Extra {
    private String rawValue;
    private String raw;
    private Object trailingComma;
}