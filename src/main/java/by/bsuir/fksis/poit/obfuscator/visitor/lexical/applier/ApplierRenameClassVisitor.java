package by.bsuir.fksis.poit.obfuscator.visitor.lexical.applier;

import by.bsuir.fksis.poit.obfuscator.state.lexical.ConvertedClassInf;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.typesystem.Type;


public class ApplierRenameClassVisitor extends VoidVisitorAdapter<JavaParserFacade> {

    private ConvertedClassInf convertedClassInf;

    public ApplierRenameClassVisitor(ConvertedClassInf convertedClassInf) {
        this.convertedClassInf = convertedClassInf;
    }


    public void visit(ClassOrInterfaceDeclaration declaration, JavaParserFacade javaParserFacade) {
        NodeList<ClassOrInterfaceType> extendTypes = declaration.getExtendedTypes();
        NodeList<ClassOrInterfaceType> implementsInterfaces = declaration.getImplementedTypes();

        for (ClassOrInterfaceType type : extendTypes) {
            String oldClassName = javaParserFacade.convert(type.getElementType(), declaration).asReferenceType().getQualifiedName();
            if (convertedClassInf.containsFullClassName(oldClassName)) {
                type.setName(convertedClassInf.getNewFullClassName(oldClassName));
            }
        }

        for (ClassOrInterfaceType inter : implementsInterfaces) {
            String oldClassName = javaParserFacade.convertToUsage(inter.getElementType(), declaration).asReferenceType().getQualifiedName();
            if (convertedClassInf.containsFullClassName(oldClassName)) {
                inter.setName(convertedClassInf.getNewFullClassName(oldClassName));
            }
        }
        super.visit(declaration, javaParserFacade);
    }

    public void visit(MethodDeclaration methodDeclaration, JavaParserFacade facade) {

        Type methodType = facade.convert(methodDeclaration.getType(), methodDeclaration);

        if (methodType.isReferenceType()) {
            String fullTypeName = methodType.asReferenceType().getQualifiedName();

            if (convertedClassInf.containsFullClassName(fullTypeName)) {
                methodDeclaration.setType(convertedClassInf.getNewFullClassName(fullTypeName));
            }
        }


        for (Parameter parameter : methodDeclaration.getParameters()) {
            Type paramType = facade.convertToUsage(parameter.getType(), parameter);
            if (paramType.isReferenceType()) {
                String fullParameterType = paramType.asReferenceType().getQualifiedName();
                if (convertedClassInf.containsFullClassName(fullParameterType)) {
                    parameter.setType(convertedClassInf.getNewFullClassName(fullParameterType));
                }
            }

        }
    }

    public void visit(FieldDeclaration fieldDeclaration, JavaParserFacade facade) {
        Type fieldType = facade.convertToUsage(fieldDeclaration.getVariables().get(0).getType(), fieldDeclaration);
        if (fieldType.isReferenceType()) {
            String fullClassName = fieldType.asReferenceType().getQualifiedName();
            if (convertedClassInf.containsFullClassName(fullClassName)) {
                VariableDeclarator variableDeclarator = fieldDeclaration.getVariable(0);
                variableDeclarator.setType(convertedClassInf.getNewFullClassName(fullClassName));
            }
        }
    }

    public void visit(VariableDeclarator variableDeclarator, JavaParserFacade facade) {
        Type type = facade.getType(variableDeclarator);
        if (type.isReferenceType()) {
            String fullClassName = type.asReferenceType().getQualifiedName();
            if (convertedClassInf.containsFullClassName(fullClassName)) {
                variableDeclarator.setType(convertedClassInf.getNewFullClassName(fullClassName));
            }
        }
    }


    public void visit(CastExpr castExpr, JavaParserFacade facade) {
        Type type = facade.getType(castExpr);
        if (type.isReferenceType()) {
            String fullClassName = type.asReferenceType().getQualifiedName();
            if (convertedClassInf.containsFullClassName(fullClassName)) {
                castExpr.setType(convertedClassInf.getNewFullClassName(fullClassName));
            }
        }
    }

    public void visit(InstanceOfExpr instanceOfExpr, JavaParserFacade facade) {
        Type type = facade.getType(instanceOfExpr);
        if (type.isReferenceType()) {
            String fullClassName = type.asReferenceType().getQualifiedName();
            if (convertedClassInf.containsFullClassName(fullClassName)) {
                instanceOfExpr.setType(convertedClassInf.getNewFullClassName(fullClassName));
            }
        }
    }

    public void visit(WildcardType wildcard, JavaParserFacade facade) {

    }


}
