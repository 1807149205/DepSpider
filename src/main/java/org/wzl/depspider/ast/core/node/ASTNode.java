package org.wzl.depspider.ast.core.node;

/**
 * AST顶层节点
 *
 * @author weizhilong
 */
public interface ASTNode {

    <T> T accept(ASTVisitor<T> visitor);

}
