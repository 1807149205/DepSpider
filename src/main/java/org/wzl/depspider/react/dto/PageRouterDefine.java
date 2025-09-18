package org.wzl.depspider.react.dto;

import lombok.Data;

import java.io.File;

/**
 * 页面路由定义
 *
 * @author 卫志龙
 */
@Data
public class PageRouterDefine {

    /**
     * 一般是 path
     */
    private String routePath;

    /**
     * 路由的具体文件
     */
    private File componentFile;

    /**
     * 一般是路由的名称，如果没有定义，则为空
     */
    private String title;

}
