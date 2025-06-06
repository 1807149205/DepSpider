package org.wzl.depspider.ast.jsx.parser.node;

import org.wzl.depspider.ast.core.node.ASTVisitor;
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.ImportDeclarationNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.specifier.Specifier;

/**
 * JSX语法树节点访问器
 * @param <T>   JSX节点Node
 *
 * @author weizhilong
 */
public interface JSXNodeVisitor<T> extends ASTVisitor<T> {

    T visit(FileNode fileNode);

    T visit(ImportDeclarationNode importDeclarationNode);

    T visit(Specifier specifier);

    T visit(ProgramNode programNode);
}
