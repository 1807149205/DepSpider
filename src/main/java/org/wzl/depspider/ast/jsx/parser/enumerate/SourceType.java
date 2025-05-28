package org.wzl.depspider.ast.jsx.parser.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 该类型用在FileNode的type类型中。
 * @see org.wzl.depspider.ast.jsx.parser.node.FileNode
 *
 * 该类型用于指定当前解析的文件使用的哪种JavaScript模块 [ module | script ]。
 *
 * @author weizhilong
 */
@Getter
@AllArgsConstructor
public enum SourceType {

    /**
     * ES Module 文件，即使用 import 和 export 语法的模块化文件。
     */
    MODULE("module"),

    /**
     * 普通的脚本文件，不支持模块语法，不能使用 import/export。
     */
    SCRIPT("script");

    private final String type;

}
