package org.wzl.depspider.ast.core.tokenizer;

import java.util.List;

/**
 * Tokenizer 接口定义了将 JavaScript 源代码解析为 Token 的基本操作。
 * 实现类需提供源码输入、Token 解析与遍历的逻辑。
 *
 * @author weizhilong
 */
public interface Tokenizer {

    /**
     * 设置源代码输入内容。
     *
     * @param source JavaScript 源代码字符串
     * @author weizhilong
     */
    void setSource(String source);

    /**
     * 解析整个源代码，并返回所有 Token。
     *
     * @return 按顺序返回的 Token 列表
     * @author weizhilong
     */
    List<Token> tokenize();

    /**
     * 判断是否已到达源代码末尾。
     *
     * @return 若无更多 Token，则返回 true
     * @author weizhilong
     */
    boolean isAtEnd();
}
