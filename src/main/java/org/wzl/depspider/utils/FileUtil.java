package org.wzl.depspider.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {

    public static String getInputString(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            bufferedReader.lines().forEach(e -> stringBuilder.append(e).append("\n"));
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws Exception {
        String inputString = getInputString("/Users/weizhilong/VscodeProjects/test-react-project/src/App.jsx");
        System.out.println(inputString);
    }

}
