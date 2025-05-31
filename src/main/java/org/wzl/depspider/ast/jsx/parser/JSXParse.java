package org.wzl.depspider.ast.jsx.parser;

import lombok.extern.slf4j.Slf4j;
import org.wzl.depspider.ast.core.tokenizer.Token;
import org.wzl.depspider.ast.exception.CodeIllegalException;
import org.wzl.depspider.ast.jsx.parser.node.FileNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.Loc;
import org.wzl.depspider.ast.jsx.parser.node.definition.Node;
import org.wzl.depspider.ast.jsx.tokenizer.JSXTokenizer;
import org.wzl.depspider.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class JSXParse {

    private List<Node> tokens = new ArrayList<>();

    private JSXTokenizer JSXTokenizer = new JSXTokenizer();

    private List<Node> nodes = new ArrayList<>();

    public JSXParse(String filePath) {
        String inputString = "";
        try {
            inputString = FileUtil.getInputString(filePath);
        } catch (Exception e) {
            log.info("code file not found");
            throw new CodeIllegalException("code file not found");
        }
        JSXTokenizer.setSource(inputString);

        List<Token> tokenize = JSXTokenizer.tokenize();
        tokenize.forEach(System.out::println);
        parse();
    }

    public FileNode parse() {
        int fileStartIndex = 0, fileEndIndex = 0;
        FileNode fileNode = new FileNode(fileStartIndex, fileEndIndex, new Loc());

        return null;
    }

}
