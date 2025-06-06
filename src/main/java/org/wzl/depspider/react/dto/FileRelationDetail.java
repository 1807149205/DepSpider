package org.wzl.depspider.react.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class FileRelationDetail extends ProjectFileRelation{

    private Map<String, List<String>> importMap;

}
