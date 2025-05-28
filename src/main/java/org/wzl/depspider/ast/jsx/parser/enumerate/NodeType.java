package org.wzl.depspider.ast.jsx.parser.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * JSX文件的节点类型
 *
 * @author weizhilong
 */
@Getter
@AllArgsConstructor
public enum NodeType {

    FILE("File"),
    PROGRAM("Program"),;

    private final String type;

}
