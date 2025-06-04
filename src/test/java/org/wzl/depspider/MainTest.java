package org.wzl.depspider;

import org.junit.Test;
import org.wzl.depspider.react.dto.ProjectFileRelation;
import org.wzl.depspider.react.project.JSXFileOperation;
import org.wzl.depspider.react.project.config.Language;
import org.wzl.depspider.react.project.config.ProjectConfiguration;
import org.wzl.depspider.react.project.ReactProjectOperator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainTest {

    @Test
    public void t1(){
        ProjectConfiguration projectConfiguration = new ProjectConfiguration();
        List<Language> languages = new ArrayList<>();
        languages.add(Language.JS);
        languages.add(Language.TS);
        projectConfiguration.setLanguages(languages);
        List<String> scanPath = new ArrayList<>();
        scanPath.add("src");
        scanPath.add("pages");
//        scanPath.add("site-6s-management");
        projectConfiguration.setScanPath(scanPath);
        ReactProjectOperator reactProjectOperator = new ReactProjectOperator(
                "/Users/weizhilong/Desktop/DepSpiderDemo/yinhe",
                projectConfiguration
        );
        List<ProjectFileRelation> projectFileRelations = reactProjectOperator.projectFileRelation();
        // /Users/weizhilong/Desktop/DepSpiderDemo/yinhe/src/pages/scoreAnalysis/index.jsx 有误
        System.out.println(projectFileRelations);
    }

    @Test
    public void t2() {
        JSXFileOperation jsxFileOperation = new JSXFileOperation(
                "/Users/weizhilong/Desktop/DepSpiderDemo/yinhe/src/pages/scoreAnalysis/index.jsx"
        );
        List<JSXFileOperation.ImportInfo> importInfos = jsxFileOperation.importInfo();
        importInfos.forEach(System.out::println);
    }

    @Test
    public void t3() {
        File folder = new File("/Users/weizhilong/Desktop/DepSpiderDemo/yinhe/src/pages");
        int fileCount = countFiles(folder);
        System.out.println("文件总数: " + fileCount);
    }

    // 递归计数
    public static int countFiles(File folder) {
        int count = 0;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    count++;
                } else if (file.isDirectory()) {
                    count += countFiles(file);
                }
            }
        }
        return count;
    }

}
