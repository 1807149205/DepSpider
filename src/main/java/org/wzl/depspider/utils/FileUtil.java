package org.wzl.depspider.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * File工具类
 *
 * @author weizhilong
 */
public class FileUtil {

    public static String getInputString(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            bufferedReader.lines().forEach(e -> stringBuilder.append(e).append("\n"));
        }
        return stringBuilder.toString();
    }

    /**
     * 给定一个文件夹（File 对象）和一个路径（List<String> 表示子目录层级），然后进入该文件夹下指定的子目录。
     * @param rootFile      基础文件夹
     * @param pathSegments  进入的文件夹列表
     * @return
     */
    public static File resolvePath(File rootFile, List<String> pathSegments) {
        File file = rootFile;
        for (String pathSegment : pathSegments) {
            file = new File(file, pathSegment);
            if (!file.isDirectory()) {
                throw new RuntimeException("文件不存在");
            }
        }
        return file;
    }

    /**
     * 校验文件和文件后缀名
     * @param filePath      文件路径
     * @param fileSuffix    文件后缀名
     */
    public static void validateFile(String filePath, String fileSuffix) {
        if (null == filePath || filePath.isEmpty()) {
            throw new RuntimeException("文件路径不能为空");
        }
        if (null == fileSuffix || fileSuffix.isEmpty()) {
            throw new RuntimeException("文件后缀名不能为空");
        }

        if (!filePath.endsWith(fileSuffix)) {
            throw new RuntimeException("文件后缀名不匹配，期望后缀名为：" + fileSuffix);
        }
        if (!new java.io.File(filePath).exists()) {
            throw new RuntimeException("文件不存在");
        }
        if (!new java.io.File(filePath).isFile()) {
            throw new RuntimeException("文件不存在");
        }
    }

    public static void main(String[] args) throws Exception {
        String inputString = getInputString("/Users/weizhilong/VscodeProjects/test-react-project/src/App.jsx");
        System.out.println(inputString);
    }

}
