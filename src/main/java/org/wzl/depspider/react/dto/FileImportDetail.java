package org.wzl.depspider.react.dto;

import lombok.Data;

import java.util.List;

@Data
public class FileImportDetail {

    private String importPath;

    private List<String> importItems;

}
