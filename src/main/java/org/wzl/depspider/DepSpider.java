package org.wzl.depspider;

import com.alibaba.fastjson2.JSON;
import org.wzl.depspider.ast.core.tokenizer.Token;
import org.wzl.depspider.ast.core.tokenizer.Tokenizer;
import org.wzl.depspider.ast.jsx.parser.JSXParse;
import org.wzl.depspider.ast.jsx.parser.node.FileNode;
import org.wzl.depspider.ast.jsx.tokenizer.JSXTokenizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * DepSpider Main class
 */
public class DepSpider {

    public static void main(String[] args) {
        String file = "/Users/weizhilong/VscodeProjects/test-react-project/src/App.jsx";
        JSXParse jsxParse = new JSXParse(file);
        FileNode parse = jsxParse.parse();
    }

}
