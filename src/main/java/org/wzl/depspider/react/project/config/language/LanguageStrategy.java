package org.wzl.depspider.react.project.config.language;

import java.io.File;

/**
 * 不同语言的操作顶层接口
 *
 * @author weizhilong
 */
public interface LanguageStrategy {

    /**
     * 获取基于folder的Index子文件，该函数将会在folder后 + index.jsx 或 index.js
     * @param folder    目录
     * @return          index文件，后缀名由子类决定，如果没有对应的index文件，则返回null
     */
    File createNewChildIndexFile(File folder);

    /**
     * 获取当前目录下，folder下有没有这个文件
     * eg. folder: src/pages  fileName: const   result: src/pages/const.js
     * @param folder        当前的目录
     * @param fileName      文件名
     * @return              目标文件，如果没有，则为null
     */
    File createNewChildWithPrefix(File folder, String fileName);
}
