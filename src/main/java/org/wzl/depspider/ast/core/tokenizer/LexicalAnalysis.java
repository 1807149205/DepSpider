package org.wzl.depspider.ast.core.tokenizer;

import java.io.InputStream;
import java.util.List;

/**
 * 词法分析
 * 将源代码转换成词法单元
 *
 * @param <T> 词法单元类型
 * @author weizhilong
 */
public interface LexicalAnalysis<T> {

    /**
     * 分析代码
     * @param code  代码文件
     * @return      词法分析结果
     */
    List<T> analyze(String code);

    /**
     * 分析代码
     * @param in    代码文件流
     * @return      词法分析结果
     */
    List<T> analyze(InputStream in);

}
