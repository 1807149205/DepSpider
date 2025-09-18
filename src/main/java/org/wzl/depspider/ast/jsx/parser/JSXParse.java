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
import org.wzl.depspider.ast.jsx.parser.node.definition.ArrayExpression;
import org.wzl.depspider.ast.jsx.parser.node.definition.Expression;
import org.wzl.depspider.ast.jsx.parser.node.definition.ObjectExpression;
import org.wzl.depspider.ast.jsx.parser.node.definition.ObjectProperty;
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
                        Node variableDeclaration = variableDeclaration(token);
                        body.add(variableDeclaration);
                    }
                }
                //变量/普通标识符

            }

            stack.push(nextToken());
        }
        return body;
    }

    private Node variableDeclaration(Token kindToken) {
        List<VariableDeclarator> declarators = new ArrayList<>();

        int declarationStart = kindToken.getStartIndex();
        Position declarationStartPos = new Position(kindToken.getLine(), kindToken.getColumn(), kindToken.getStartIndex());
        Token lastToken = kindToken;

        while (!isAtEnd()) {
            Token identifierToken = nextToken();
            if (identifierToken == null) {
                break;
            }
            if (!identifierToken.getType().equals(JSXToken.Type.IDENTIFIER)) {
                lastToken = identifierToken;
                if (identifierToken.getType().equals(JSXToken.Type.EOF)) {
                    break;
                }
                continue;
            }

            Identifier identifier = buildIdentifier(identifierToken);
            lastToken = identifierToken;

            Node initNode = null;
            Token equalsToken = peekToken();
            if (equalsToken != null && equalsToken.getType().equals(JSXToken.Type.OPERATOR) && "=".equals(equalsToken.getValue())) {
                nextToken();
                Token initToken = nextToken();
                if (initToken != null) {
                    ParsedNode parsedNode = parseInitializer(initToken);
                    initNode = parsedNode.node;
                    if (parsedNode.lastToken != null) {
                        lastToken = parsedNode.lastToken;
                    } else {
                        lastToken = initToken;
                    }
                }
            }

            VariableDeclarator declarator = new VariableDeclarator(
                    identifierToken.getStartIndex(),
                    lastToken.getEndIndex(),
                    new Loc(
                            new Position(identifierToken.getLine(), identifierToken.getColumn(), identifierToken.getStartIndex()),
                            new Position(lastToken.getLine(), lastToken.getColumn(), lastToken.getEndIndex())
                    )
            );
            declarator.setId(identifier);
            declarator.setInit(initNode);
            declarator.setKind(kindToken.getValue());
            declarators.add(declarator);

            Token separator = peekToken();
            if (separator != null && separator.getType().equals(JSXToken.Type.COMMA)) {
                lastToken = nextToken();
                continue;
            }
            break;
        }

        Position endPos = new Position(lastToken.getLine(), lastToken.getColumn(), lastToken.getEndIndex());
        VariableDeclarationNode node = new VariableDeclarationNode(
                declarationStart,
                lastToken.getEndIndex(),
                new Loc(declarationStartPos, endPos)
        );
        node.setDeclarations(declarators);
        return node;
    }

    private ParsedNode parseInitializer(Token initToken) {
        TokenType tokenType = initToken.getType();
        if (tokenType.equals(JSXToken.Type.STRING)) {
            return new ParsedNode(getStringLiteral(initToken), initToken);
        }
        if (tokenType.equals(JSXToken.Type.NUMBER)) {
            return new ParsedNode(getNumericLiteral(initToken), initToken);
        }
        if (tokenType.equals(JSXToken.Type.IDENTIFIER)) {
            if (isArrowFunctionWithSingleParam()) {
                Token last = skipArrowFunctionBody(initToken);
                return new ParsedNode(null, last);
            }
            return new ParsedNode(buildIdentifier(initToken), initToken);
        }
        if (tokenType.equals(JSXToken.Type.LEFT_BRACE)) {
            return parseObjectExpression(initToken);
        }
        if (tokenType.equals(JSXToken.Type.LEFT_PARENTHESIS)) {
            Token last = skipArrowFunctionWithParentheses(initToken);
            return new ParsedNode(null, last);
        }
        if (tokenType.equals(JSXToken.Type.KEYWORD) && "function".equals(initToken.getValue())) {
            Token last = skipFunctionExpression();
            return new ParsedNode(null, last);
        }
        if (tokenType.equals(JSXToken.Type.LEFT_BRACKET)) {
            return parseArrayExpression(initToken);
        }
        return new ParsedNode(null, initToken);
    }

    private NumericLiteral getNumericLiteral(Token numberToken) {
        return new NumericLiteral(
                numberToken.getStartIndex(),
                numberToken.getEndIndex(),
                new Loc(
                        new Position(numberToken.getLine(), numberToken.getColumn(), numberToken.getStartIndex()),
                        new Position(numberToken.getLine(), numberToken.getColumn(), numberToken.getEndIndex())
                )
        );
    }

    private Identifier buildIdentifier(Token token) {
        return new Identifier(
                token.getStartIndex(),
                token.getEndIndex(),
                new Loc(
                        new Position(token.getLine(), token.getColumn(), token.getStartIndex()),
                        new Position(token.getLine(), token.getColumn(), token.getEndIndex())
                ),
                token.getValue()
        );
    }

    private ParsedNode parseObjectExpression(Token leftBraceToken) {
        List<ObjectProperty> properties = new ArrayList<>();
        Token lastToken = leftBraceToken;

        while (!isAtEnd()) {
            Token token = nextToken();
            if (token == null) {
                break;
            }
            if (token.getType().equals(JSXToken.Type.RIGHT_BRACE)) {
                lastToken = token;
                break;
            }
            if (token.getType().equals(JSXToken.Type.COMMA)) {
                continue;
            }
            if (!token.getType().equals(JSXToken.Type.IDENTIFIER) && !token.getType().equals(JSXToken.Type.STRING)) {
                lastToken = token;
                continue;
            }

            Token keyToken = token;
            Token valueToken = peekToken();
            Node valueNode = null;
            Token propertyEndToken = keyToken;
            boolean hasExplicitValue = false;

            Node keyValue = keyToken.getType().equals(JSXToken.Type.STRING)
                    ? getStringLiteral(keyToken)
                    : buildIdentifier(keyToken);

            if (valueToken != null) {
                TokenType valueType = valueToken.getType();
                if (!valueType.equals(JSXToken.Type.COMMA) && !valueType.equals(JSXToken.Type.RIGHT_BRACE)) {
                    Token firstValueToken = nextToken();
                    ParsedNode parsedValue = parseInitializer(firstValueToken);
                    hasExplicitValue = true;
                    if (parsedValue != null) {
                        if (parsedValue.node != null) {
                            valueNode = parsedValue.node;
                        }
                        if (parsedValue.lastToken != null) {
                            propertyEndToken = parsedValue.lastToken;
                        } else {
                            propertyEndToken = firstValueToken;
                        }
                    } else {
                        propertyEndToken = firstValueToken;
                    }
                }
            }

            ObjectProperty property = new ObjectProperty(
                    keyToken.getStartIndex(),
                    propertyEndToken.getEndIndex(),
                    new Loc(
                            new Position(keyToken.getLine(), keyToken.getColumn(), keyToken.getStartIndex()),
                            new Position(propertyEndToken.getLine(), propertyEndToken.getColumn(), propertyEndToken.getEndIndex())
                    )
            );
            property.setMethod(false);
            property.setComputed(false);
            property.setShorthand(!hasExplicitValue);
            property.setKey(keyValue);
            property.setValue(valueNode);
            properties.add(property);
            lastToken = propertyEndToken;
        }

        ObjectExpression objectExpression = new ObjectExpression(
                leftBraceToken.getStartIndex(),
                lastToken.getEndIndex(),
                new Loc(
                        new Position(leftBraceToken.getLine(), leftBraceToken.getColumn(), leftBraceToken.getStartIndex()),
                        new Position(lastToken.getLine(), lastToken.getColumn(), lastToken.getEndIndex())
                )
        );
        objectExpression.setProperties(properties);
        return new ParsedNode(objectExpression, lastToken);
    }

    private ParsedNode parseArrayExpression(Token leftBracketToken) {
        List<Expression> elements = new ArrayList<>();
        Token lastToken = leftBracketToken;

        while (!isAtEnd()) {
            Token token = nextToken();
            if (token == null) {
                break;
            }

            if (token.getType().equals(JSXToken.Type.RIGHT_BRACKET)) {
                lastToken = token;
                break;
            }

            if (token.getType().equals(JSXToken.Type.COMMA) || token.getType().equals(JSXToken.Type.COMMENT)) {
                continue;
            }

            ParsedNode parsedElement = parseArrayElement(token);
            if (parsedElement != null) {
                if (parsedElement.node instanceof Expression) {
                    elements.add((Expression) parsedElement.node);
                }
                if (parsedElement.lastToken != null) {
                    lastToken = parsedElement.lastToken;
                } else {
                    lastToken = token;
                }
            } else {
                lastToken = token;
            }
        }

        ArrayExpression arrayExpression = new ArrayExpression(
                leftBracketToken.getStartIndex(),
                lastToken.getEndIndex(),
                new Loc(
                        new Position(leftBracketToken.getLine(), leftBracketToken.getColumn(), leftBracketToken.getStartIndex()),
                        new Position(lastToken.getLine(), lastToken.getColumn(), lastToken.getEndIndex())
                )
        );
        arrayExpression.setElements(elements);
        return new ParsedNode(arrayExpression, lastToken);
    }

    private ParsedNode parseArrayElement(Token firstToken) {
        TokenType type = firstToken.getType();
        if (type.equals(JSXToken.Type.STRING)) {
            return new ParsedNode(getStringLiteral(firstToken), firstToken);
        }
        if (type.equals(JSXToken.Type.NUMBER)) {
            return new ParsedNode(getNumericLiteral(firstToken), firstToken);
        }
        if (type.equals(JSXToken.Type.IDENTIFIER)) {
            if (isArrowFunctionWithSingleParam()) {
                Token last = skipArrowFunctionBody(firstToken);
                return new ParsedNode(null, last);
            }
            return new ParsedNode(buildIdentifier(firstToken), firstToken);
        }
        if (type.equals(JSXToken.Type.LEFT_BRACE)) {
            return parseObjectExpression(firstToken);
        }
        if (type.equals(JSXToken.Type.LEFT_BRACKET)) {
            return parseArrayExpression(firstToken);
        }
        if (type.equals(JSXToken.Type.LEFT_PARENTHESIS)) {
            Token last = skipArrowFunctionWithParentheses(firstToken);
            return new ParsedNode(null, last);
        }
        if (type.equals(JSXToken.Type.KEYWORD) && "function".equals(firstToken.getValue())) {
            Token last = skipFunctionExpression();
            return new ParsedNode(null, last);
        }

        Token last = skipExpressionAfterFirst(firstToken);
        return new ParsedNode(null, last);
    }

    private boolean isArrowFunctionWithSingleParam() {
        Token arrowStart = peekToken();
        Token arrowEnd = peekNextToken();
        return arrowStart != null
                && arrowEnd != null
                && arrowStart.getType().equals(JSXToken.Type.OPERATOR)
                && "=".equals(arrowStart.getValue())
                && arrowEnd.getType().equals(JSXToken.Type.OPERATOR_OR_JSX_TAG_START)
                && ">".equals(arrowEnd.getValue());
    }

    private Token skipArrowFunctionBody(Token parameterToken) {
        nextToken(); // consume '=' from '=>'
        Token gtToken = nextToken();
        Token lastToken = gtToken != null ? gtToken : parameterToken;

        Token bodyStart = nextToken();
        if (bodyStart == null) {
            return lastToken;
        }

        lastToken = bodyStart;
        if (bodyStart.getType().equals(JSXToken.Type.LEFT_BRACE)) {
            lastToken = consumeBalanced(bodyStart, JSXToken.Type.LEFT_BRACE, JSXToken.Type.RIGHT_BRACE);
        } else {
            lastToken = skipExpressionAfterFirst(bodyStart);
        }
        return lastToken;
    }

    private Token skipArrowFunctionWithParentheses(Token leftParenToken) {
        Token lastToken = consumeBalanced(leftParenToken, JSXToken.Type.LEFT_PARENTHESIS, JSXToken.Type.RIGHT_PARENTHESIS);
        Token arrowEq = peekToken();
        if (arrowEq != null && arrowEq.getType().equals(JSXToken.Type.OPERATOR) && "=".equals(arrowEq.getValue())) {
            nextToken();
            Token arrowGt = nextToken();
            lastToken = arrowGt != null ? arrowGt : leftParenToken;
            Token bodyStart = nextToken();
            if (bodyStart != null) {
                if (bodyStart.getType().equals(JSXToken.Type.LEFT_BRACE)) {
                    lastToken = consumeBalanced(bodyStart, JSXToken.Type.LEFT_BRACE, JSXToken.Type.RIGHT_BRACE);
                } else {
                    lastToken = skipExpressionAfterFirst(bodyStart);
                }
            } else {
                lastToken = arrowGt != null ? arrowGt : leftParenToken;
            }
        }
        return lastToken;
    }

    private Token skipFunctionExpression() {
        Token lastToken = null;
        Token maybeName = peekToken();
        if (maybeName != null && maybeName.getType().equals(JSXToken.Type.IDENTIFIER)) {
            lastToken = nextToken();
        }

        Token paramsStart = peekToken();
        if (paramsStart != null && paramsStart.getType().equals(JSXToken.Type.LEFT_PARENTHESIS)) {
            lastToken = consumeBalanced(nextToken(), JSXToken.Type.LEFT_PARENTHESIS, JSXToken.Type.RIGHT_PARENTHESIS);
        }

        Token bodyStart = peekToken();
        if (bodyStart != null && bodyStart.getType().equals(JSXToken.Type.LEFT_BRACE)) {
            lastToken = consumeBalanced(nextToken(), JSXToken.Type.LEFT_BRACE, JSXToken.Type.RIGHT_BRACE);
        }

        return lastToken;
    }

    private Token consumeBalanced(Token openingToken, TokenType openType, TokenType closeType) {
        int depth = 1;
        Token lastToken = openingToken;
        while (!isAtEnd() && depth > 0) {
            Token token = nextToken();
            if (token == null) {
                break;
            }
            lastToken = token;
            if (token.getType().equals(openType)) {
                depth++;
            } else if (token.getType().equals(closeType)) {
                depth--;
            }
        }
        return lastToken;
    }

    private Token skipExpressionAfterFirst(Token firstToken) {
        Token lastToken = firstToken;
        int braceDepth = firstToken.getType().equals(JSXToken.Type.LEFT_BRACE) ? 1 : 0;
        int parenDepth = firstToken.getType().equals(JSXToken.Type.LEFT_PARENTHESIS) ? 1 : 0;
        int bracketDepth = firstToken.getType().equals(JSXToken.Type.LEFT_BRACKET) ? 1 : 0;

        while (!isAtEnd()) {
            Token next = peekToken();
            if (next == null) {
                break;
            }

            if (braceDepth == 0 && parenDepth == 0 && bracketDepth == 0) {
                TokenType type = next.getType();
                if (type.equals(JSXToken.Type.COMMA) || type.equals(JSXToken.Type.KEYWORD) || type.equals(JSXToken.Type.EOF)) {
                    break;
                }
            }

            Token consumed = nextToken();
            if (consumed == null) {
                break;
            }
            lastToken = consumed;

            if (consumed.getType().equals(JSXToken.Type.LEFT_BRACE)) {
                braceDepth++;
            } else if (consumed.getType().equals(JSXToken.Type.RIGHT_BRACE)) {
                if (braceDepth == 0) {
                    break;
                }
                braceDepth--;
                if (braceDepth == 0) {
                    Token potentialBreak = peekToken();
                    if (potentialBreak == null) {
                        break;
                    }
                    if (potentialBreak.getType().equals(JSXToken.Type.COMMA) || potentialBreak.getType().equals(JSXToken.Type.KEYWORD)) {
                        break;
                    }
                }
            } else if (consumed.getType().equals(JSXToken.Type.LEFT_PARENTHESIS)) {
                parenDepth++;
            } else if (consumed.getType().equals(JSXToken.Type.RIGHT_PARENTHESIS)) {
                if (parenDepth > 0) {
                    parenDepth--;
                }
            } else if (consumed.getType().equals(JSXToken.Type.LEFT_BRACKET)) {
                bracketDepth++;
            } else if (consumed.getType().equals(JSXToken.Type.RIGHT_BRACKET)) {
                if (bracketDepth == 0) {
                    break;
                }
                bracketDepth--;
                if (bracketDepth == 0) {
                    Token potentialBreak = peekToken();
                    if (potentialBreak == null) {
                        break;
                    }
                    TokenType potentialType = potentialBreak.getType();
                    if (potentialType.equals(JSXToken.Type.COMMA) || potentialType.equals(JSXToken.Type.KEYWORD)) {
                        break;
                    }
                }
            }
        }
        return lastToken;
    }

    private static class ParsedNode {
        private final Node node;
        private final Token lastToken;

        private ParsedNode(Node node, Token lastToken) {
            this.node = node;
            this.lastToken = lastToken;
        }
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
