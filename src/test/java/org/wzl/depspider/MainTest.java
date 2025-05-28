package org.wzl.depspider;

import org.junit.Test;
import org.wzl.depspider.ast.jsx.parser.JSXParse;
import org.wzl.depspider.ast.jsx.parser.node.FileNode;


public class MainTest {

    @Test
    public void t1() {
        JSXParse jsxParse = new JSXParse("D:\\gitlab\\yinhe\\src\\pages\\detail\\index.jsx");
        FileNode parse = jsxParse.parse();
    }

}
