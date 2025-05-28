package org.wzl.depspider.ast.jsx.parser.node.definition;

import lombok.Getter;
import lombok.Setter;
import org.wzl.depspider.ast.jsx.parser.enumerate.NodeType;
import org.wzl.depspider.ast.jsx.parser.enumerate.SpecifierType;

/**
 * 标识符
 *
 * @author weizhilong
 */
@Setter
@Getter
public class Specifier extends Node {

    private SpecifierType type;

    public Specifier(SpecifierType type, int start, int end, Loc loc) {
        super(start, end, loc);
        this.type = type;
    }

}
