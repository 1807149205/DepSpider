package org.wzl.depspider.ast.jsx.parser.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wzl.depspider.ast.jsx.parser.node.definition.Identifier;
import org.wzl.depspider.ast.jsx.parser.node.definition.MemberExpression;
import org.wzl.depspider.ast.jsx.parser.node.definition.ObjectExpression;
import org.wzl.depspider.ast.jsx.parser.node.definition.ObjectProperty;
import org.wzl.depspider.ast.jsx.parser.node.definition.literal.NumericLiteral;
import org.wzl.depspider.ast.jsx.parser.node.definition.literal.StringLiteral;
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.ImportDeclarationNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.VariableDeclarationNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.VariableDeclarator;
import org.wzl.depspider.ast.jsx.parser.node.definition.specifier.ImportSpecifier;

@Getter
@AllArgsConstructor
public enum NodeType {

    FILE("File", FileNode.class),
    PROGRAM("Program", ProgramNode.class),

    IMPORT_DECLARATION("ImportDeclaration", ImportDeclarationNode.class),
    VARIABLE_DECLARATION("VariableDeclaration", VariableDeclarationNode.class),

    VARIABLE_DECLARATOR("VariableDeclarator", VariableDeclarator.class),

    OBJECT_PROPERTY("ObjectProperty", ObjectProperty.class),

    OBJECT_EXPRESSION("ObjectExpression", ObjectExpression.class),

    NUMERICL_LITERAL("NumericLiteral", NumericLiteral.class),

    IMPORT_SPECIFIER("ImportSpecifier", ImportSpecifier.class),
    IDENTIFIER("Identifier", Identifier.class),
    STRING_LITERAL("StringLiteral", StringLiteral.class),
    MEMBER_EXPRESSION("MemberExpression", MemberExpression.class);


    private final String typeName;

    private final Class<?> nodeClass;

}
