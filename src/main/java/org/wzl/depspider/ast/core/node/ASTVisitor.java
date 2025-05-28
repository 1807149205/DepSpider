package org.wzl.depspider.ast.core.node;

import org.wzl.depspider.ast.javascript.node.JavascriptFunctionNode;
import org.wzl.depspider.ast.javascript.node.JavascriptIdentifierNode;

public interface ASTVisitor<T> {

    T visit(JavascriptIdentifierNode javascriptIdentifierNode);

    T visit(JavascriptFunctionNode javascriptFunctionNode);
}
