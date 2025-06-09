package org.wzl.depspider.ast.jsx.parser;

import lombok.Getter;
import org.wzl.depspider.ast.jsx.parser.node.FileNode;
import org.wzl.depspider.ast.jsx.parser.node.JSXNodeVisitor;
import org.wzl.depspider.ast.jsx.parser.node.ProgramNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.MemberExpression;
import org.wzl.depspider.ast.jsx.parser.node.definition.Node;
import org.wzl.depspider.ast.jsx.parser.node.definition.ObjectExpression;
import org.wzl.depspider.ast.jsx.parser.node.definition.ObjectProperty;
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.ImportDeclarationNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.declaration.VariableDeclarationNode;
import org.wzl.depspider.ast.jsx.parser.node.definition.literal.NumericLiteral;
import org.wzl.depspider.ast.jsx.parser.node.definition.specifier.Specifier;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于访问 AST，收集所有 import 路径及其导入的标识符
 *
 * @author weizhilong
 */
@Getter
public class JSXImportVisitor implements JSXNodeVisitor<Void> {

    public static class ImportRecord {
        public final String sourcePath;
        public final List<String> importedNames;

        public ImportRecord(String sourcePath) {
            this.sourcePath = sourcePath;
            this.importedNames = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "Import from '" + sourcePath + "': " + importedNames;
        }
    }

    private final List<ImportRecord> imports = new ArrayList<>();

    @Override
    public Void visit(FileNode node) {
        node.getProgram().accept(this);
        return null;
    }

    @Override
    public Void visit(ProgramNode node) {
        for (Node child : node.getBody()) {
            child.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(VariableDeclarationNode variableDeclarationNode) {
        return null;
    }

    @Override
    public Void visit(ObjectExpression objectExpression) {
        return null;
    }

    @Override
    public Void visit(ObjectProperty objectProperty) {
        return null;
    }

    @Override
    public Void visit(NumericLiteral numericLiteral) {
        return null;
    }

    @Override
    public Void visit(MemberExpression memberExpression) {
        return null;
    }

    @Override
    public Void visit(ImportDeclarationNode node) {
        ImportRecord currentRecord = new ImportRecord(node.getSource().getValue());
        for (Specifier specifier : node.getSpecifiers()) {
            specifier.accept(this);
        }
        imports.add(currentRecord);
        return null;
    }

    @Override
    public Void visit(Specifier specifier) {
        return null;
    }

}
