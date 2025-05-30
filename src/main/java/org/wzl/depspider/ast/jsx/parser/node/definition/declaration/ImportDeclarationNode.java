package org.wzl.depspider.ast.jsx.parser.node.definition.declaration;


import lombok.Getter;
import lombok.Setter;
import org.wzl.depspider.ast.jsx.parser.enumerate.NodeType;
import org.wzl.depspider.ast.jsx.parser.node.definition.CommentBlock;
import org.wzl.depspider.ast.jsx.parser.node.definition.Loc;
import org.wzl.depspider.ast.jsx.parser.node.definition.Node;
import org.wzl.depspider.ast.jsx.parser.node.definition.Specifier;
import org.wzl.depspider.ast.jsx.parser.node.definition.StringLiteral;

import java.util.List;

@Getter
@Setter
public class ImportDeclarationNode extends Node {

    public String type;
    public int start;
    public int end;
    public Loc loc;
    public List<Specifier> specifiers;
    public StringLiteral source;
    public List<Object> attributes;
    public List<CommentBlock> leadingComments;

    public ImportDeclarationNode(int start, int end, Loc loc) {
        super(start, end, loc);
    }

}
