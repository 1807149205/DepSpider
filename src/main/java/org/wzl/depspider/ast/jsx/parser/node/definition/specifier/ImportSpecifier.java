package org.wzl.depspider.ast.jsx.parser.node.definition.specifier;

import lombok.Getter;
import lombok.Setter;
import org.wzl.depspider.ast.jsx.parser.enumerate.NodeType;
import org.wzl.depspider.ast.jsx.parser.enumerate.SpecifierType;
import org.wzl.depspider.ast.jsx.parser.node.definition.Identifier;
import org.wzl.depspider.ast.jsx.parser.node.definition.Loc;

/**
 * 导入声明
 *
 * @author weizhilong
 */
@Setter
@Getter
public class ImportSpecifier extends Specifier {

    public int start;
    public int end;
    public Loc loc;
    public Identifier imported;
    public Identifier local;

    public ImportSpecifier(SpecifierType type, int start, int end, Loc loc) {
        super(type, start, end, loc);
    }
}
