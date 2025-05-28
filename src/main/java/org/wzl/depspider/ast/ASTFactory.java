package org.wzl.depspider.ast;

/**
 * AST工厂类
 *
 * @author weizhilong
 */
public class ASTFactory {

    private static class Holder {
        private static final ASTFactory instance = new ASTFactory();
    }

    private ASTFactory() {
    }

    public static ASTFactory getInstance() {
        return Holder.instance;
    }

}
