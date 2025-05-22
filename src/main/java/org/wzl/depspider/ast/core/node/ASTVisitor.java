package org.wzl.depspider.ast.core.node;

import org.wzl.depspider.ast.javascript.node.JavascriptFunctionNode;
import org.wzl.depspider.ast.javascript.node.JavascriptIdentifierNode;
import org.wzl.depspider.ast.jsx.node.JSXAttributeNode;
import org.wzl.depspider.ast.jsx.node.JSXElementNode;
import org.wzl.depspider.ast.jsx.node.JSXExpressionNode;
import org.wzl.depspider.ast.jsx.node.JSXFragmentNode;
import org.wzl.depspider.ast.jsx.node.JSXRootNode;
import org.wzl.depspider.ast.jsx.node.JSXTextNode;

public interface ASTVisitor<T> {

    T visit(JSXAttributeNode jsxAttributeNode);

    T visit(JSXElementNode jsxElementNode);

    T visit(JSXExpressionNode jsxExpressionNode);

    T visit(JSXTextNode jsxTextNode);

    T visit(JSXFragmentNode jsxFragmentNode);

    T visit(JSXRootNode jsxRootNode);

    T visit(JavascriptIdentifierNode javascriptIdentifierNode);

    T visit(JavascriptFunctionNode javascriptFunctionNode);
}
