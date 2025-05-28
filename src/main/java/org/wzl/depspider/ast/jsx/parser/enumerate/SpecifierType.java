package org.wzl.depspider.ast.jsx.parser.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 导入声明的类型
 *
 * @author weizhilong
 */
@Getter
@AllArgsConstructor
public enum SpecifierType {

    /**
     * 默认导入
     * 例如： import a from 'a'
     */
    IMPORT_DEFAULT_SPECIFIER("ImportDefaultSpecifier"),

    /**
     * 命名导入
     * 例如： import {a} from 'a'
     */
    IMPORT_SPECIFIER("ImportSpecifier"),
    ;

    private final String type;

}
