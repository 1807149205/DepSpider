package org.wzl.depspider.react.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFileRelation {

    /**
     * 代码文件
     */
    private File targetFile;

    /**
     * 所关联的文件路径
     */
    private List<File> relationFilePaths;

}
