package org.wzl.depspider;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class JsRunnerWithGraalVM {
    public static void main(String[] args) throws IOException {
        File file = new File(
                "D:\\gitlab\\yinhe", "src" + File.separator + "pages"
        );
        File file1 = new File(file, "aaa");

        File codeFile = new File("D:\\gitlab\\yinhe\\src\\pages\\fiveStarManage\\index.jsx");
        String source = "./components/scoreDiagnosis";
        String[] split = source.split("/");
        codeFile = codeFile.getParentFile();
        for (int i = 1 ; i < split.length ; i++) {
            codeFile = new File(codeFile, split[i]);
        }
        System.out.println(codeFile);
    }
}
