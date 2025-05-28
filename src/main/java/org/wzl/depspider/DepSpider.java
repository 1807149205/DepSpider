package org.wzl.depspider;

import com.alibaba.fastjson2.JSON;
import org.wzl.depspider.ast.core.tokenizer.Token;
import org.wzl.depspider.ast.core.tokenizer.Tokenizer;
import org.wzl.depspider.ast.jsx.tokenizer.JSXTokenizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * DepSpider Main class
 */
public class DepSpider {

    public static void main(String[] args) {
        String filePath = "D:\\gitlab\\yinhe\\src\\App.jsx"; // 文件路径
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                contentBuilder.append(currentLine).append(System.lineSeparator()); // 拼接每一行
            }
//            System.out.println(contentBuilder.toString()); // 输出文件内容
        } catch (IOException e) {
            e.printStackTrace(); // 异常处理
        }

        Tokenizer tokenizer = new JSXTokenizer();

        tokenizer.setSource(contentBuilder.toString());
        List<Token> tokenize = tokenizer.tokenize();
        System.out.println(tokenize);

        // 将字符串写入 a.json 文件
        String outputFilePath = "./a.json";
        try {
            Files.write(Paths.get(outputFilePath), JSON.toJSONString(tokenize).getBytes());
            System.out.println("文件已成功写入: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace(); // 异常处理
        }
    }

}
