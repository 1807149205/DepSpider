package org.wzl.depspider.ast.core.tokenizer;

/**
 * 顶层Token接口
 *
 * @author weizhilong
 */
public interface Token {

    /**
     * 获取Token类型
     * @return  Token类型
     */
    TokenType getType();

    /**
     * 获取Token值
     * @return token值
     */
    String getValue();

    /**
     * 获取该Token的起始位置
     * @return 代码文件中的起始位置
     */
    int getStartIndex();

    /**
     * 获取该Token的结束位置
     * @return 代码文件中的结束位置
     */
    int getEndIndex();

    /**
     * 获取该Token的行数
     * @return  代码文件中的行数
     */
    int getLine();

    /**
     * 获取该token起始的token所在的列下标
     * @return  代码文件中token所在的token下标
     */
    int getColumn();
}
