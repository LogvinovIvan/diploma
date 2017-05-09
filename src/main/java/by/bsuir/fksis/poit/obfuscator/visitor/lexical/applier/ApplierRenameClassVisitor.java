package by.bsuir.fksis.poit.obfuscator.visitor.lexical.applier;

import by.bsuir.fksis.poit.obfuscator.state.lexical.ConvertedClassInf;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.typesystem.Type;


public class ApplierRenameClassVisitor extends VoidVisitorAdapter<JavaParserFacade> {

    private ConvertedClassInf convertedClassInf;

    public ApplierRenameClassVisitor(ConvertedClassInf convertedClassInf) {
        this.convertedClassInf = convertedClassInf;
    }


    public void visit(ClassOrInterfaceDeclaration declaration, JavaParserFacade javaParserFacade) {

        super.visit(declaration, javaParserFacade);
        String oldClassName = javaParserFacade.getTypeOfThisIn(declaration).asReferenceType().getQualifiedName();
        String newClassName = defineNewClassName(oldClassName, declaration.getNameAsString());
        if (newClassName != null && !newClassName.isEmpty()) {
            declaration.setName(defineNewClassName(oldClassName, declaration.getNameAsString()));
            declaration.getNodesByType(ConstructorDeclaration.class).forEach(
                    declaration1 -> {
                        visit(declaration1, newClassName, javaParserFacade);
                    }
            );
        }

    }

    //
//    public void visit(MethodDeclaration methodDeclaration, JavaParserFacade facade) {
//
//        Type methodType = facade.convert(methodDeclaration.getType(), methodDeclaration);
//
//        if (methodType.isReferenceType()) {
//            String fullTypeName = methodType.asReferenceType().getQualifiedName();
//
//            if (convertedClassInf.containsFullClassName(fullTypeName)) {
//                methodDeclaration.setType(defineNewClassName(fullTypeName, methodDeclaration.getType().toString()));
//            }
//        }
//
//
//        for (Parameter parameter : methodDeclaration.getParameters()) {
//            Type paramType = facade.convertToUsage(parameter.getType(), parameter);
//            if (paramType.isReferenceType()) {
//                String fullParameterType = paramType.asReferenceType().getQualifiedName();
//                if (convertedClassInf.containsFullClassName(fullParameterType)) {
//                    parameter.setType(defineNewClassName(fullParameterType, parameter.getType().toString()));
//                }
//            }
//
//        }
//
//        super.visit(methodDeclaration, facade);
//    }
//
//    public void visit(FieldDeclaration fieldDeclaration, JavaParserFacade facade) {
//        Type fieldType = facade.convertToUsage(fieldDeclaration.getVariables().get(0).getType(), fieldDeclaration);
//        if (fieldType.isReferenceType()) {
//            String fullClassName = fieldType.asReferenceType().getQualifiedName();
//            if (convertedClassInf.containsFullClassName(fullClassName)) {
//                fieldDeclaration.getVariables().forEach(
//                        variableDeclarator -> visit(variableDeclarator, facade)
//                );
//            }
//        }
//        super.visit(fieldDeclaration,facade);
//    }
//
//    public void visit(VariableDeclarator variableDeclarator, JavaParserFacade facade) {
//        try {
//            Type type = facade.convertToUsage(variableDeclarator.getType(), variableDeclarator);
//            if (type.isReferenceType()) {
//                String fullClassName = type.asReferenceType().getQualifiedName();
//                if (convertedClassInf.containsFullClassName(fullClassName)) {
//                    variableDeclarator.setType(convertedClassInf.getNewFullClassName(fullClassName));
//                }
//            }
//            super.visit(variableDeclarator, facade);
//        } catch (Exception e) {
//            System.out.println(variableDeclarator.getName());
//        }
//
//    }
//
//
//    public void visit(CastExpr castExpr, JavaParserFacade facade) {
//        Type type = facade.convertToUsage(castExpr.getType(), castExpr);
//        if (type.isReferenceType()) {
//            String fullClassName = type.asReferenceType().getQualifiedName();
//            if (convertedClassInf.containsFullClassName(fullClassName)) {
//                castExpr.setType(defineNewClassName(fullClassName, castExpr.getType().toString()));
//            }
//        }
//        super.visit(castExpr, facade);
//    }
//
//    public void visit(InstanceOfExpr instanceOfExpr, JavaParserFacade facade) {
//        Type type = facade.convertToUsage(instanceOfExpr.getType(), instanceOfExpr);
//        if (type.isReferenceType()) {
//            String fullClassName = type.asReferenceType().getQualifiedName();
//            if (convertedClassInf.containsFullClassName(fullClassName)) {
//                instanceOfExpr.setType(defineNewClassName(fullClassName, instanceOfExpr.getType().toString()));
//            }
//        }
//
//        super.visit(instanceOfExpr, facade);
//    }
//
    private void visit(ConstructorDeclaration declaration, String newClassName, JavaParserFacade facade) {
        declaration.setName(newClassName);
        declaration.getNodesByType(Parameter.class).forEach(
                parameter -> {
                    Type paramType = facade.convertToUsage(parameter.getType(), parameter);
                    if (paramType.isReferenceType()) {
                        String fullParameterType = paramType.asReferenceType().getQualifiedName();
                        if (convertedClassInf.containsFullClassName(fullParameterType)) {
                            parameter.setType(defineNewClassName(fullParameterType, parameter.getType().toString()));
                        }
                    }
                }
        );
    }

    //
//
//    public void visit(ObjectCreationExpr expr, JavaParserFacade facade) {
////        System.out.println(expr);
//        Type type = facade.convertToUsage(expr.getType(), expr);
//        if(type.isReferenceType()){
//            String fullType = type.asReferenceType().getQualifiedName();
//            if (convertedClassInf.containsFullClassName(fullType)) {
//                expr.setType(defineNewClassName(fullType, expr.getType().toString()));
//            }
//        }
//        super.visit(expr,facade);
//    }
//
//
//
//
    private String defineNewClassName(String oldClassName, String currenName) {
        String newClassName;
        if (!oldClassName.equals(currenName)) {
            newClassName = convertedClassInf.getNewClassName(oldClassName);
        } else {
            newClassName = convertedClassInf.getNewFullClassName(oldClassName);
        }
        return newClassName;
    }

    public void visit(ClassOrInterfaceType classOrInterfaceType, JavaParserFacade facade) {
        try {
            Type type = facade.convertToUsage(classOrInterfaceType.getElementType(), classOrInterfaceType);
            if (type.isReferenceType()) {
                String fullType = type.asReferenceType().getQualifiedName();
                if (convertedClassInf.containsFullClassName(fullType)) {
                    classOrInterfaceType.setName(defineNewClassName(fullType, classOrInterfaceType.toString()));
                }
            }

        } catch (Exception e) {
            System.out.println(classOrInterfaceType);
        }
        super.visit(classOrInterfaceType, facade);

    }


}
