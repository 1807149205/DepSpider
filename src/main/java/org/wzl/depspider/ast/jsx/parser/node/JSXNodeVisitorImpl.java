package org.wzl.depspider.ast.jsx.parser.node;

import lombok.extern.slf4j.Slf4j;
import org.wzl.depspider.ast.jsx.parser.node.definition.Node;
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.ImportDeclarationNode;
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
}
