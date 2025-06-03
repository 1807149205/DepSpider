package org.wzl.depspider.react.dto;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class ProjectFileRelation {

    /**
     * 代码
     */
    private File targetFilePath;

    /**
     * 所关联的文件路径
     */
    private List<File> relationFilePaths;

}
