package org.wzl.depspider.react.project.config.language;

import java.io.File;

public class TSLanguageStrategy implements LanguageStrategy {

    @Override
    public File createNewChildIndexFile(File folder) {
        File file = new File(folder, "index.ts");
        if (file.isFile()) {
            return file;
        }
        File file1 = new File(folder, "index.tsx");
        if (file1.isFile()) {
            return file1;
        } else {
            return null;
        }
    }

    @Override
    public File createNewChildWithPrefix(File folder, String fileName) {
        File file = new File(folder, fileName + ".ts");
        if (file.isFile()) {
            return file;
        }
        File file1 = new File(folder, fileName + ".tsx");
        if (file1.isFile()) {
            return file1;
        } else {
            return null;
        }
    }
}
