package org.wzl.depspider.ast.jsx.parser;

import org.wzl.depspider.ast.jsx.parser.node.FileNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.Node;
import org.wzl.depspider.ast.jsx.tokenizer.JSXTokenizer;

import java.util.List;

public class JSXParse {

    private List<Node> tokens;

    private JSXTokenizer JSXTokenizer;

    public JSXParse(String filePath) {

    }

    public FileNode parse() {
        int fileStartIndex = 0, fileEndIndex = 0;
        FileNode fileNode = new FileNode(fileStartIndex, fileEndIndex, null);

        return null;
    }

}
