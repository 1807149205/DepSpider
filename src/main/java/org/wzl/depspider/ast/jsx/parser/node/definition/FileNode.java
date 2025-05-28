package org.wzl.depspider.ast.jsx.parser.node.definition;

import java.util.List;

public class FileNode {
    public String type;
    public int start;
    public int end;
    public Loc loc;
    public List<Object> errors; // 这里只是空数组，可以按需定义为 List<ErrorType>
    public ProgramNode program;
}
