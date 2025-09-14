package org.wzl.depspider.react.dto;

import java.io.File;
import java.util.List;

/**
 * 代码文件依赖节点
 *
 * @author 卫志龙
 * @date 2025/9/11 17:53
 */
public class RelationTreeNode extends File {

    /**
     * 子节点
     */
    private List<RelationTreeNode> children;

    public RelationTreeNode(String pathname) {
        super(pathname);
    }
}
