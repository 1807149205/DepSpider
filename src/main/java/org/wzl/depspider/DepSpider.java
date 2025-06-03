package org.wzl.depspider;

import org.wzl.depspider.react.JSXFileOperation;
import org.wzl.depspider.react.dto.ProjectFileRelation;
import org.wzl.depspider.react.project.ProjectConfiguration;
import org.wzl.depspider.react.project.ReactProjectOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * DepSpider Main class
 */
public class DepSpider {

    public static void main(String[] args) {
//        String file = "/Users/weizhilong/VscodeProjects/test-react-project/src/App.jsx";
//        JSXParse jsxParse = new JSXParse(file);
//        FileNode parse = jsxParse.parse();
//        List<Node> body = parse.getProgram().getBody();
//        body.forEach(System.out::println);

        JSXFileOperation jsxFileOperation = new JSXFileOperation(
                "D:\\gitlab\\yinhe\\src\\pages\\list\\index.jsx"
        );
        List<JSXFileOperation.ImportInfo> importInfos = jsxFileOperation.importInfo();
        System.out.println(importInfos);

        ProjectConfiguration projectConfiguration = new ProjectConfiguration();
        List<ProjectConfiguration.Language> languages = new ArrayList<>();
        languages.add(ProjectConfiguration.Language.JS);
        languages.add(ProjectConfiguration.Language.TS);
        projectConfiguration.setLanguages(languages);
        List<String> scanPaths = new ArrayList<>();
        scanPaths.add("src");
        scanPaths.add("pages");
        projectConfiguration.setScanPath(scanPaths);

        ReactProjectOperator reactProjectOperator = new ReactProjectOperator(
                "D:\\gitlab\\yinhe",
                projectConfiguration
        );
        List<ProjectFileRelation> projectFileRelations = reactProjectOperator.projectFileRelation();
        System.out.println(projectFileRelations);
    }

}
