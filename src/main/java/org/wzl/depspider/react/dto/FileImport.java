package org.wzl.depspider.react.dto;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class FileImport {

    private File file;

    private List<FileImportDetail> imports;

}
