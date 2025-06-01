package org.wzl.depspider.ast.jsx.parser.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wzl.depspider.ast.jsx.parser.node.definition.Identifier;
import org.wzl.depspider.ast.jsx.parser.node.definition.StringLiteral;
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.ImportDeclarationNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.specifier.ImportSpecifier;

@Getter
@AllArgsConstructor
public enum NodeType {

    FILE("File", FileNode.class),
    PROGRAM("Program", ProgramNode.class),
    IMPORT_DECLARATION("ImportDeclaration", ImportDeclarationNode.class),
    IMPORT_SPECIFIER("ImportSpecifier", ImportSpecifier.class),
    IDENTIFIER("Identifier", Identifier.class),
    STRING_LITERAL("StringLiteral", StringLiteral.class),;


    private final String typeName;

    private final Class<?> nodeClass;

}
