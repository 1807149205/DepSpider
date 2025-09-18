package org.wzl.depspider.ast.jsx.parser.node;

import lombok.extern.slf4j.Slf4j;
import org.wzl.depspider.ast.jsx.parser.node.definition.ArrayExpression;
import org.wzl.depspider.ast.jsx.parser.node.definition.MemberExpression;
import org.wzl.depspider.ast.jsx.parser.node.definition.Node;
import org.wzl.depspider.ast.jsx.parser.node.definition.ObjectExpression;
import org.wzl.depspider.ast.jsx.parser.node.definition.ObjectProperty;
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.ImportDeclarationNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.VariableDeclarationNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.literal.NumericLiteral;
import org.wzl.depspider.ast.jsx.parser.node.definition.specifier.Specifier;

/**
 * JSX节点访问器
 * @param <T>   JSX节点Node
 *
 * @author weizhilong
 */
@Slf4j
public class JSXNodeVisitorImpl<T> implements JSXNodeVisitor<T> {

    @Override
    public T visit(FileNode fileNode) {
        fileNode.getProgram().accept(this);
        return null;
    }

    @Override
    public T visit(ImportDeclarationNode importDeclarationNode) {
        for (Node node : importDeclarationNode.getSpecifiers()) {
            node.accept(this);
        }
        return null;
    }

    @Override
    public T visit(Specifier specifier) {
        specifier.accept(this);
        return null;
    }

    @Override
    public T visit(ProgramNode programNode) {
        for (Node node : programNode.getBody()) {
            node.accept(this);
        }
        return null;
    }

    @Override
    public T visit(VariableDeclarationNode variableDeclarationNode) {
        return null;
    }

    @Override
    public T visit(ObjectExpression objectExpression) {
        return null;
    }

    @Override
    public T visit(ObjectProperty objectProperty) {
        return null;
    }

    @Override
    public T visit(NumericLiteral numericLiteral) {
        return null;
    }

    @Override
    public T visit(MemberExpression memberExpression) {
        return null;
    }

    @Override
    public T visit(ArrayExpression arrayExpression) {
        return null;
    }
}
