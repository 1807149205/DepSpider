package org.wzl.depspider.ast.jsx.parser;

import lombok.Getter;
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
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.VariableDeclarator;
import org.wzl.depspider.ast.jsx.parser.node.definition.literal.NumericLiteral;
import org.wzl.depspider.ast.jsx.parser.node.definition.literal.StringLiteral;
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.ImportDeclarationNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.VariableDeclarationNode;
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

    @Getter
    private Boolean isImportOnly = false;

    /**
     * 当前token的索引位置
     */
    private int tokenIndex = 0;


    private final static List<String> VARIABLE_KEY_WORD =
            new ArrayList<>();
    static {
        VARIABLE_KEY_WORD.add("const");
        VARIABLE_KEY_WORD.add("let");
        VARIABLE_KEY_WORD.add("var");
    }

    /**
     * 获取下一个token，并且index++
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
     * 获取下一个token，index不变
     * @return  Token
     */
    protected Token peekNextToken() {
        if (tokenIndex + 1 >= tokenSize) {
            return null;
        }
        return tokens.get(tokenIndex + 1);
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
        return getFileNode();
    }

    public FileNode parse(Boolean isImportOnly) {
        this.isImportOnly = isImportOnly;
        return getFileNode();
    }

    private FileNode getFileNode() {
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
                    if (isImportOnly && !value.equals("import")) {
                        break;
                    }
                    if (VARIABLE_KEY_WORD.contains(value)) {
                        Node variableDeclaration = variableDeclaration();
                        body.add(variableDeclaration);
                    }
                }
                //变量/普通标识符

            }

            stack.push(nextToken());
        }
        return body;
    }

    private Node variableDeclaration() {
        //改变量是否为函数
        boolean isFunc = false;
        int nodeStartIndex = 0;
        int nodeEndIndex = 0;
        int startLine = 0;
        int startColumn = 0;
        int startIndex = 0;
        int endLine = 0;
        int endColumn = 0;
        int endIndex = 0;
        Position startNodePos = new Position(
                startLine, startColumn, startIndex
        );
        Position endNodePos = new Position(
                endLine, endColumn, endIndex
        );

        List<VariableDeclarator> declarators = new ArrayList<>();

        //变量有两种
        //  第一种：变量
        //      1.const a = 1
        //      2.const a = 1, b = "", c = {}
        //      3.const a = {}
        //      4.const a = ""
        //      5.const a = `${exp}`
        //  第二种：函数
        //      1.const a = () => {}
        //      2.const a = function () {}

        //读取第一个变量名
        Token variableName = nextToken();

        nextToken();    //equalToken =

        Token varOrFuncToken = nextToken();
        TokenType varOrFuncTokenType = varOrFuncToken.getType();

        //函数 1 const a = () => {}
        if (varOrFuncTokenType.equals(JSXToken.Type.LEFT_PARENTHESIS)) {
            isFunc = true;
            nextToken();    // )
            nextToken();    // =
            nextToken();    // >
            nextToken();    // {

//            while (!nextToken().getType().equals(JSXToken.Type.RIGHT_BRACE)) {
//                Token token = nextToken();
//
//            }
        }

        //函数 2 const a = function () {}
        if (varOrFuncTokenType.equals(JSXToken.Type.KEYWORD)) {
            isFunc = true;

        }

        //变量 1 const a = 1
        if (varOrFuncTokenType.equals(JSXToken.Type.NUMBER)) {
            VariableDeclarator declarator = new VariableDeclarator(
                    varOrFuncToken.getStartIndex(),
                    varOrFuncToken.getEndIndex(),
                    new Loc(
                            new Position(
                                    varOrFuncToken.getLine(),
                                    varOrFuncToken.getColumn(),
                                    varOrFuncToken.getStartIndex()
                            ),
                            new Position(
                                    varOrFuncToken.getLine(),
                                    varOrFuncToken.getColumn(),
                                    varOrFuncToken.getEndIndex()
                            )
                    )
            );
            declarators.add(declarator);

        }

        //变量 3 const a = {}
        if (varOrFuncTokenType.equals(JSXToken.Type.LEFT_BRACE)) {

        }

        //变量4 const a = "xxx"
        if (varOrFuncTokenType.equals(JSXToken.Type.STRING)) {
            Token var = nextToken();
            int start = 0;
            int end = 0;
            VariableDeclarator declarator = new VariableDeclarator(
                start, end, new Loc()
            );
            declarator.setId(
                    new Identifier(
                            variableName.getStartIndex(),
                            variableName.getEndIndex(),
                            new Loc(),
                            variableName.getValue()
                    )
            );
            declarator.setInit(getStringLiteral(var));
            declarator.setKind("const");
        }

        //变量 2 const a = 1, b = "", c = {}
        if (!isFunc && peekToken().getType().equals(JSXToken.Type.COMMA)) {
            while (!peekToken().getType().equals(JSXToken.Type.COMMA)) {
                Token varValue = nextToken();   //变量名
                nextToken();                    // =
                Token var = nextToken();        //变量

                VariableDeclarator declarator = new VariableDeclarator(
                        varValue.getStartIndex(),
                        var.getEndIndex(),
                        new Loc(
                                new Position(varValue.getLine(), varValue.getColumn(), varValue.getEndIndex()),
                                new Position(var.getLine(), var.getColumn(), var.getEndIndex())
                        )
                );
                declarator.setId(new Identifier(
                        varValue.getStartIndex(),
                        varValue.getEndIndex(),
                        new Loc(
                                new Position(varValue.getLine(), varValue.getColumn(), varValue.getStartIndex()),
                                new Position(varValue.getLine(), varValue.getColumn(), varValue.getEndIndex())
                        ),
                        varValue.getValue()
                ));
                declarator.setInit(getVarInit());
                declarators.add(declarator);
            }
        }


        VariableDeclarationNode node = new VariableDeclarationNode(
                nodeStartIndex,
                nodeEndIndex,
                new Loc(startNodePos, endNodePos)
        );
        node.setDeclarations(declarators);
        return node;
    }

    /**
     * 获取变量的值
     * jsx文件的变量有如下几种：
     * 1、字符串    const a = "1" '1' `1`
     * 2、数字     const a = 1 1.1
     * 3、对象     const a = { a:{}, a:function(){}, a:"1" }
     * 4、函数     const a = () => {}  const a = function() {}
     * @return eg. const a = "1", return (StringLiteral)"1"
     *         return Type:
     *         1、VariableDeclarator
     *         2、StringLiteral
     *
     */
    private Node getVarInit() {
        Token oneToken = nextToken();
        //字符串
        if (oneToken.getType().equals(JSXToken.Type.STRING)) {
            return getStringLiteral(oneToken);
        }
        //数字
        if (oneToken.getType().equals(JSXToken.Type.NUMBER)) {
            Token numberToken = nextToken();
            return new NumericLiteral(
                    numberToken.getStartIndex(),
                    numberToken.getEndIndex(),
                    new Loc(
                            new Position(numberToken.getLine(), numberToken.getColumn(), numberToken.getStartIndex()),
                            new Position(numberToken.getLine(), numberToken.getColumn(), numberToken.getEndIndex())
                    )
            );
        }
        //对象
        if (oneToken.getType().equals(JSXToken.Type.LEFT_BRACE)) {

        }
        return null;
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
            ImportSpecifier importSpecifier = new ImportSpecifier(
                    SpecifierType.IMPORT_DEFAULT_SPECIFIER,
                    startIndex,
                    peekToken.getEndIndex(),
                    new Loc(
                            new Position(peekToken.getLine(), peekToken.getColumn(), peekToken.getStartIndex()),
                            new Position(peekToken.getLine(), peekToken.getColumn(), peekToken.getEndIndex())
                    )
            );
            importSpecifier.setImported(new Identifier(
                    peekToken.getStartIndex(),
                    peekToken.getEndIndex(),
                    new Loc(
                            new Position(peekToken.getLine(), peekToken.getColumn(), peekToken.getStartIndex()),
                            new Position(peekToken.getLine(), peekToken.getColumn(), peekToken.getEndIndex())
                    ),
                    peekToken.getValue()
            ));
            specifiers.add(importSpecifier);

            //当 import React, { CSSProperties } from 'react' 这种情况时
            if (peekNextToken().getType().equals(JSXToken.Type.LEFT_BRACE)) {
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
                    Specifier importSpecifierCur = getImportSpecifier(importedToken);
                    specifiers.add(importSpecifierCur);
                }
            }
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
                sourceToken.getStartIndex(),
                0, // 结束位置待定
                new Loc(
                        new Position(sourceToken.getLine(), sourceToken.getColumn(), sourceToken.getStartIndex()),
                        new Position(sourceToken.getLine(), sourceToken.getColumn(), sourceToken.getEndIndex())
                ),
                Extra.builder()
                        .raw(value).rawValue(value)
                        .build(),
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
