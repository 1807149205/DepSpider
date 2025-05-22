package org.wzl.depspider.ast.jsx.parser;

import lombok.extern.slf4j.Slf4j;
import org.wzl.depspider.ast.jsx.node.JSXASTNode;
import org.wzl.depspider.ast.jsx.node.JSXRootNode;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JSXParser {

    /**
     * 解析Jsx文件
     * @param jsx   一个完整的jsx文件
     */
    public JSXRootNode parse(String jsx) {
        List<JSXASTNode> children = new ArrayList<>();
        JSXRootNode root = new JSXRootNode(children);



        return root;
    }

}
