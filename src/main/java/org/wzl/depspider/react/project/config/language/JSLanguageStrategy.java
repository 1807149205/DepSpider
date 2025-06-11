package org.wzl.depspider.react.project.config.language;

import java.io.File;

public class JSLanguageStrategy implements LanguageStrategy{

    @Override
    public File createNewChildIndexFile(File folder) {
        File file = new File(folder, "index.js");
        if (file.isFile()) {
            return file;
        }
        File file1 = new File(folder, "index.jsx");
        if (file1.isFile()) {
            return file1;
        } else {
            return null; // 如果没有index.js或index.jsx文件，则返回null
        }
    }

    @Override
    public File createNewChildWithPrefix(File folder, String fileName) {
        File file = new File(folder, fileName + ".js");
        if (file.isFile()) {
            return file;
        }
        File file1 = new File(folder, fileName + ".jsx");
        if (file1.isFile()) {
            return file1;
        } else {
            return null;
        }
    }
}
