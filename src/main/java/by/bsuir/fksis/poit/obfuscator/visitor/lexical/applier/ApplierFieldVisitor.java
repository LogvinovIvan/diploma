package by.bsuir.fksis.poit.obfuscator.visitor.lexical.applier;

import by.bsuir.fksis.poit.obfuscator.state.lexical.ConvertedClassInf;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Иван on 15.04.2017.
 */
public class ApplierFieldVisitor extends VoidVisitorAdapter<JavaParserFacade> {

    private Map<String, String> mapFields;
    private List<String> fields = new ArrayList<>();
    private String fullClassName;
    private String simpleClassName;
    private final static Integer LENGTH_NAME_PARAMETER = 7;
    private Set<String> fieldCurrentClass = new HashSet<>();
    private ConvertedClassInf convertedClassInf;

    public ApplierFieldVisitor(ConvertedClassInf convertedClassInf) {
        this.convertedClassInf = convertedClassInf;
    }

    public void visit(ClassOrInterfaceDeclaration declaration, JavaParserFacade javaParserFacade) {
        this.fullClassName = javaParserFacade.getTypeDeclaration(declaration).getQualifiedName();
        this.simpleClassName = declaration.getNameAsString();
        if (convertedClassInf.getLexicalClassNameInf(fullClassName) == null) {
            System.out.println(declaration);
        }
        mapFields = convertedClassInf.getLexicalClassNameInf(fullClassName).getFields();
        super.visit(declaration, javaParserFacade);
    }

    public void visit(FieldDeclaration fieldDeclaration, JavaParserFacade facade) {
        if (fieldDeclaration.isPrivate()) {
            List<VariableDeclarator> declarators = fieldDeclaration.getVariables();
            declarators.forEach(
                    variableDeclarator -> {

                        String fullFieldName = variableDeclarator.getNameAsString();
                        fields.add(fullFieldName);
                        variableDeclarator.setName(mapFields.get(fullFieldName));

                        String type = variableDeclarator.getType().toString();
                        if (type.equals(fullClassName)) {
                            fieldCurrentClass.add(fullFieldName);
                        }
                    }
            );
        }
        super.visit(fieldDeclaration, facade);
    }


    @Override
    public void visit(FieldAccessExpr fieldAccessExpr, JavaParserFacade facade) {
        Expression expression = fieldAccessExpr.getScope().orElseGet(null);
        if (expression.toString().contains("this.") || expression.toString().equals("this")) {
            String name = fieldAccessExpr.getNameAsString();
            if (mapFields.containsKey(name)) {
                fieldAccessExpr.setName(mapFields.get(name));
            }
        }
        super.visit(fieldAccessExpr, facade);
    }

    @Override
    public void visit(MethodDeclaration declaration, JavaParserFacade facade) {
        super.visit(declaration, facade);
        Set<Parameter> parameters = declaration.getParameters().stream().collect(Collectors.toSet());

        if (!declaration.isAbstract() && declaration.getBody().isPresent()) {
            BlockStmt blockStmt = declaration.getBody().orElse(null);
            List<VariableDeclarator> variableDeclarators = blockStmt.getNodesByType(VariableDeclarator.class);
            Set<String> variables = variableDeclarators.stream().
                    map(NodeWithSimpleName::getNameAsString).collect(Collectors.toSet());

            Set<String> fieldMethods = mapFields.keySet().stream()
                    .filter(key -> !variables.contains(key) && !parameters.contains(key))
                    .collect(Collectors.toSet());

            List<Expression> expressions = declaration.getNodesByType(Expression.class);
            List<NameExpr> nameExprs = expressions.stream().
                    filter(expression -> expression instanceof NameExpr).map(expression -> (NameExpr) expression).
                    collect(Collectors.toList());


            nameExprs.forEach(expressionStmt -> {
                String expression = expressionStmt.toString();
                if (fieldMethods.contains(expression)) {
                    expressionStmt.setName(mapFields.get(expression));
                }
            });

            renameCurrentClassFieldInBlock(declaration.getBody().get(), facade, parameters);
        }

    }

    @Override
    public void visit(ConstructorDeclaration declaration, JavaParserFacade facade) {
        super.visit(declaration, facade);
        Set<String> parameters = declaration.getParameters().stream().map(parameter -> parameter.getNameAsString()).collect(Collectors.toSet());


        BlockStmt blockStmt = declaration.getBody();
        List<VariableDeclarator> variableDeclarators = blockStmt.getNodesByType(VariableDeclarator.class);
        Set<String> variables = variableDeclarators.stream().
                map(NodeWithSimpleName::getNameAsString).collect(Collectors.toSet());

        Set<String> fieldMethods = mapFields.keySet().stream()
                .filter(key -> !variables.contains(key) && !parameters.contains(key))
                .collect(Collectors.toSet());

        List<Expression> expressions = declaration.getNodesByType(Expression.class);
        List<NameExpr> nameExprs = expressions.stream().
                filter(expression -> expression instanceof NameExpr).map(expression -> (NameExpr) expression).
                collect(Collectors.toList());


        nameExprs.forEach(expressionStmt -> {
            String expression = expressionStmt.toString();
            if (fieldMethods.contains(expression)) {
                expressionStmt.setName(mapFields.get(expression));
            }
        });
        renameCurrentClassFieldInBlock(declaration.getBody(), facade, new HashSet<>(declaration.getParameters()));

    }

    private void renameCurrentClassFieldInBlock(BlockStmt blockStmt, JavaParserFacade facade, Set<Parameter> parameters) {
        List<VariableDeclarator> variableDeclarators = blockStmt.getNodesByType(VariableDeclarator.class);
        Set<String> varWithCurrentClass = new HashSet<>();
        varWithCurrentClass.addAll(
                variableDeclarators.
                        stream().
                        filter(variableDeclarator -> variableDeclarator.getType().toString().equals(simpleClassName)).
                        map(NodeWithSimpleName::getNameAsString).collect(Collectors.toSet())
        );

        varWithCurrentClass.addAll(parameters.stream().
                filter(parameter -> parameter.getType().toString().equals(simpleClassName)).
                map(NodeWithSimpleName::getNameAsString).
                collect(Collectors.toSet())
        );

        if (varWithCurrentClass.size() > 0) {
            List<FieldAccessExpr> accessExprList = blockStmt.getNodesByType(FieldAccessExpr.class);
            accessExprList.forEach(
                    fieldAccessExpr -> {
                        String scope = fieldAccessExpr.getScope().get().toString();
                        if (varWithCurrentClass.contains(scope)) {
                            String field = fieldAccessExpr.getNameAsString();
                            if (mapFields.containsKey(field)) {
                                fieldAccessExpr.setName(mapFields.get(field));
                            }
                        }
                    }
            );
        }
    }


    private String createFullNameField(String fieldName) {
        return fullClassName + fieldName;
    }

}
