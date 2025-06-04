package org.wzl.depspider.ast.jsx.parser;

import lombok.extern.slf4j.Slf4j;
import org.wzl.depspider.ast.core.tokenizer.Token;
import org.wzl.depspider.ast.core.tokenizer.TokenType;
import org.wzl.depspider.ast.exception.CodeIllegalException;
import org.wzl.depspider.ast.jsx.parser.enumerate.SourceType;
import org.wzl.depspider.ast.jsx.parser.enumerate.SpecifierType;
import org.wzl.depspider.ast.jsx.parser.node.FileNode;
import org.wzl.depspider.ast.jsx.parser.node.ProgramNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.Extra;
import org.wzl.depspider.ast.jsx.parser.node.definition.Identifier;
import org.wzl.depspider.ast.jsx.parser.node.definition.Loc;
import org.wzl.depspider.ast.jsx.parser.node.definition.Node;
import org.wzl.depspider.ast.jsx.parser.node.definition.Position;
import org.wzl.depspider.ast.jsx.parser.node.definition.StringLiteral;
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.ImportDeclarationNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.specifier.ImportSpecifier;
import org.wzl.depspider.ast.jsx.parser.node.definition.specifier.Specifier;
import org.wzl.depspider.ast.jsx.tokenizer.JSXToken;
import org.wzl.depspider.ast.jsx.tokenizer.JSXTokenizer;
import org.wzl.depspider.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Slf4j
public class JSXParse {

    private List<Token> tokens;

    private final JSXTokenizer jsxTokenizer = new JSXTokenizer();

    private final int tokenSize;

    /**
     * 当前token的索引位置
     */
    private int tokenIndex = 0;

    /**
     * 获取下一个token
     * @return  Token
     */
    protected Token nextToken() {
        if (tokenIndex >= tokenSize) {
            return null;
        }
        Token token = tokens.get(tokenIndex);
        tokenIndex++;
        return token;
    }

    /**
     * 是否到达token的末尾
     * @return  boolean
     */
    protected boolean isAtEnd() {
        return tokenIndex >= tokenSize;
    }

    /**
     * 获取当前token
     * @return Token
     */
    protected Token peekToken() {
        if (tokenIndex >= tokenSize) {
            return null;
        }
        return tokens.get(tokenIndex);
    }

    public JSXParse(String filePath) {
        String inputString;
        try {
            inputString = FileUtil.getInputString(filePath);
        } catch (Exception e) {
            log.info("code file not found");
            throw new CodeIllegalException("code file not found");
        }
        jsxTokenizer.setSource(inputString);

        tokens = jsxTokenizer.tokenize();
        tokenSize = tokens.size();
//        tokens.forEach(System.out::println);
    }

    public FileNode parse() {
        Token eofToken = tokens.get(tokenSize - 1);
        int fileStartIndex = 0, fileEndIndex = eofToken.getEndIndex();
        FileNode fileNode = new FileNode(
                fileStartIndex,
                fileEndIndex,
                new Loc(
                        new Position(0, 0, 0),
                        new Position(eofToken.getLine(), eofToken.getColumn(), eofToken.getEndIndex())
                )
        );

        ProgramNode programNode = getProgramNode(fileStartIndex, fileEndIndex, eofToken);

        fileNode.setProgram(programNode);
        return fileNode;
    }

    /**
     * 获取ProgramNode
     * @param fileStartIndex 文件开始位置
     * @param fileEndIndex 文件结束位置
     * @param eofToken 文件结束符
     * @return ProgramNode
     */
    private ProgramNode getProgramNode(int fileStartIndex, int fileEndIndex, Token eofToken) {
        ProgramNode programNode = new ProgramNode(
                fileStartIndex,
                fileEndIndex,
                new Loc(
                        new Position(0, 0, 0),
                        new Position(eofToken.getLine(), eofToken.getColumn(), eofToken.getEndIndex())
                )
        );

        //暂时设置为null
        programNode.setInterpreter(null);
        //设置源代码类型
        programNode.setSourceType(currentSourceType());
        //设置body
        programNode.setBody(getProgramBody());
        return programNode;
    }

    /**
     * 获取ProgramNode的body
     * @return List<?>
     */
    private List<Node> getProgramBody() {
        List<Node> body = new ArrayList<>();

        Stack<Token> stack = new Stack<>();
        stack.push(nextToken());
        while (!stack.isEmpty()) {
            Token token = stack.pop();
            TokenType type = token.getType();
            String value = token.getValue();

            if (type.equals(JSXToken.Type.EOF)) {// 遇到EOF，结束解析
                return body;
            }

            if (type.equals(JSXToken.Type.KEYWORD)) {
                // 关键字
                if (JSXTokenizer.KEYWORDS.contains(value)) {
                    if (value.equals("import")) {
                        Node importNode = importDeclaration();
                        body.add(importNode);
                    }
                }
                //变量/普通标识符
            }

            stack.push(nextToken());
        }
        return body;
    }

    /**
     * 处理import声明
     * @return Node
     */
    private Node importDeclaration() {
        Token peekToken = nextToken();
        TokenType type = peekToken.getType();
        int startIndex = peekToken.getStartIndex();
        //import token的顶层节点
        ImportDeclarationNode importDeclarationNode = new ImportDeclarationNode(
                startIndex,
                0,
                null //TODO: 需要设置Loc
        );
        importDeclarationNode.setType("ImportDeclaration");

        List<Specifier> specifiers = new ArrayList<>();
        importDeclarationNode.setSpecifiers(specifiers);

        /*

            1、读取导入的内容

         */

        /*
            当import语句为 import { useCallback, useActionState } from 'react' 时
         */
        if (type.equals(JSXToken.Type.LEFT_BRACE)) {
            int endIndex = 0;
            List<Token> importedTokens = new ArrayList<>();
            while (!isAtEnd()) {
                Token token = nextToken();
                if (token.getType().equals(JSXToken.Type.RIGHT_BRACE)) {
                    endIndex = token.getEndIndex();
                    break;
                }

                if (token.getType().equals(JSXToken.Type.IDENTIFIER)) {
                    importedTokens.add(token);
                }
            }

            for (Token importedToken : importedTokens) {
                Specifier importSpecifier = getImportSpecifier(importedToken);
                specifiers.add(importSpecifier);
            }

        }
        /*
            当import语句为 import reactLogo from './assets/react.svg' 时
         */
        else if (type.equals(JSXToken.Type.IDENTIFIER)) {

        }

        /*

            2、读取导入的模块路径

         */
        //设置source，也就是 import从哪里导入的
        nextToken(); //跳过 from 关键字
        Token sourceToken = nextToken();
        StringLiteral source = getStringLiteral(sourceToken);

        importDeclarationNode.setSource(source);
        return importDeclarationNode;
    }

    private static StringLiteral getStringLiteral(Token sourceToken) {
        String value = sourceToken.getValue();
        // 结束位置待定
        return new StringLiteral(
                "StringLiteral",
                sourceToken.getStartIndex(),
                0, // 结束位置待定
                new Loc(
                        new Position(sourceToken.getLine(), sourceToken.getColumn(), sourceToken.getStartIndex()),
                        new Position(sourceToken.getLine(), sourceToken.getColumn(), sourceToken.getEndIndex())
                ),
                new Extra(
                        value, value
                ),
                value
        );
    }

    /**
     * 获取ImportSpecifier
     * @param importedToken 导入的token
     * @return ImportSpecifier
     */
    private static Specifier getImportSpecifier(Token importedToken) {
        ImportSpecifier importSpecifier = new ImportSpecifier(
                SpecifierType.IMPORT_SPECIFIER,
                importedToken.getStartIndex(),
                importedToken.getEndIndex(),
                new Loc(
                        new Position(importedToken.getLine(), importedToken.getColumn(), importedToken.getStartIndex()),
                        new Position(importedToken.getLine(), importedToken.getColumn(), importedToken.getEndIndex())
                )
        );
        importSpecifier.setImported(new Identifier(
                "Identifier",
                importedToken.getStartIndex(),
                importedToken.getEndIndex(),
                new Loc(
                        new Position(importedToken.getLine(), importedToken.getColumn(), importedToken.getStartIndex()),
                        new Position(importedToken.getLine(), importedToken.getColumn(), importedToken.getEndIndex())
                ),
                importedToken.getValue()
        ));

        return importSpecifier;
    }

    /**
     * 获取当前的源代码类型
     *  1、module类型  2、script类型
     * @return SourceType
     */
    private SourceType currentSourceType() {
        //TODO: 目前只支持module类型
        return SourceType.MODULE;
    }

}
