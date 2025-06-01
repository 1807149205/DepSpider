package org.wzl.depspider;

import org.wzl.depspider.ast.jsx.parser.JSXParse;
import org.wzl.depspider.ast.jsx.parser.node.FileNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.Node;

import java.util.List;

/**
 * DepSpider Main class
 */
public class DepSpider {

    public static void main(String[] args) {
        String file = "/Users/weizhilong/VscodeProjects/test-react-project/src/App.jsx";
        JSXParse jsxParse = new JSXParse(file);
        FileNode parse = jsxParse.parse();
        List<Node> body = parse.getProgram().getBody();
        body.forEach(System.out::println);
    }

}
