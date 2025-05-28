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
     * 获取下一个 Token（不移除，预读）。
     *
     * @return 下一个 Token，如果已结束则返回 EOF 类型
     * @author weizhilong
     */
    Token peek();

    /**
     * 获取下一个 Token 并移动游标。
     *
     * @return 当前 Token，之后位置向前推进
     * @author weizhilong
     */
    Token next();

    /**
     * 判断是否已到达源代码末尾。
     *
     * @return 若无更多 Token，则返回 true
     * @author weizhilong
     */
    boolean isAtEnd();
}
