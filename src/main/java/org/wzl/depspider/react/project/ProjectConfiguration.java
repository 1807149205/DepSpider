package org.wzl.depspider.react.project;

import lombok.Data;

import java.util.List;

@Data
public class ProjectConfiguration {

    public enum Language {
        JS, TS
    }

    /**
     * 需要扫描的路径
     * 如果为null，则默认从src开始扫描
     */
    private List<String> scanPath;

    /**
     * 项目所用的语言
     */
    private List<Language> languages;

}
