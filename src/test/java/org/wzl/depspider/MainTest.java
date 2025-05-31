package org.wzl.depspider;

import org.junit.Test;
import org.wzl.depspider.ast.jsx.parser.JSXParse;
import org.wzl.depspider.ast.jsx.parser.node.FileNode;


public class MainTest {

    @Test
    public void t1() {
        String file = "/Users/weizhilong/VscodeProjects/test-react-project/src/App.jsx";
        JSXParse jsxParse = new JSXParse(file);
        FileNode parse = jsxParse.parse();
    }

}
