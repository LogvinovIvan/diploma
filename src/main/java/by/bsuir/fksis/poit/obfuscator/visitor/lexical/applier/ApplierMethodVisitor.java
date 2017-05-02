package by.bsuir.fksis.poit.obfuscator.visitor.lexical.applier;

import by.bsuir.fksis.poit.obfuscator.util.comparator.MethodUsageComparator;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.methods.MethodUsage;
import com.github.javaparser.symbolsolver.model.typesystem.Type;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Иван on 15.04.2017.
 */
public class ApplierMethodVisitor extends VoidVisitorAdapter<JavaParserFacade> {

    private HashMap<String, String> mapOfMethods;
    private Set<MethodUsage> methodUsages = new TreeSet<>(new MethodUsageComparator());
    private Set<String> methodsOfClassObject = new HashSet<>();

    {
        methodsOfClassObject.add("clone");
        methodsOfClassObject.add("toString");
        methodsOfClassObject.add("equals");
        methodsOfClassObject.add("hashCode");
        methodsOfClassObject.add("finalize");
    }


    public ApplierMethodVisitor(HashMap<String, String> mapOfMethods) {
        this.mapOfMethods = mapOfMethods;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, JavaParserFacade javaParserFacade) {

        List<ClassOrInterfaceType> extentsTypes = classOrInterfaceDeclaration.getExtendedTypes();
        for (ClassOrInterfaceType extendType : extentsTypes) {
            methodUsages.addAll(javaParserFacade.convertToUsage(extendType).asReferenceType().getDeclaredMethods());
        }

        List<ClassOrInterfaceType> implementedInterfaces = classOrInterfaceDeclaration.getImplementedTypes();
        for (ClassOrInterfaceType implementedInterface : implementedInterfaces) {
            methodUsages.addAll(javaParserFacade.convertToUsage(implementedInterface).asReferenceType().getDeclaredMethods());
        }


        super.visit(classOrInterfaceDeclaration, javaParserFacade);
        classOrInterfaceDeclaration.getMethods().forEach(methodDeclaration -> {
            visitMethod(methodDeclaration, javaParserFacade);
        });
    }


    private void visitMethod(MethodDeclaration methodDeclaration, JavaParserFacade facade) {

        if (methodDeclaration.isAnnotationPresent(Override.class) && !methodsOfClassObject.contains(methodDeclaration.getNameAsString())) {
            List<Type> types = new ArrayList<>();
            methodDeclaration.getParameters().forEach(
                    parameter -> {
                        Type type = facade.getType(parameter);
                        types.add(type);
                    }
            );

            String name = methodDeclaration.getNameAsString();
            MethodUsage usage = facade.getSymbolSolver().solveMethod(name, types, methodDeclaration);

            if (methodUsages.contains(usage)) {
                MethodUsage reqSignature = methodUsages.stream().filter(
                        methodUsage -> {
                            MethodUsageComparator methodUsageComparator = new MethodUsageComparator();
                            return methodUsageComparator.compare(methodUsage, usage) == 0;
                        }
                ).findFirst().get();

                String methodName = reqSignature.getQualifiedSignature();
                String newName = mapOfMethods.get(methodName);
                methodDeclaration.setName(newName);
            }
        } else if (!methodsOfClassObject.contains(methodDeclaration.getNameAsString())) {

            List<Type> types = new ArrayList<>();
            methodDeclaration.getParameters().forEach(
                    parameter -> {
                        Type type = facade.getType(parameter);
                        types.add(type);
                    }
            );

            String name = methodDeclaration.getNameAsString();
            MethodUsage usage = facade.getSymbolSolver().solveMethod(name, types, methodDeclaration);

            if (!methodUsages.contains(usage)) {
                String newMethodName = mapOfMethods.get(usage.getQualifiedSignature());
                methodDeclaration.setName(newMethodName);
            }
        }

    }


    @Override
    public void visit(MethodCallExpr methodCallExpr, JavaParserFacade facade) {
        try {
            String methodFullName = facade.solve(methodCallExpr).getCorrespondingDeclaration().getQualifiedSignature();
            if (mapOfMethods.keySet().contains(methodFullName)) {
                methodCallExpr.setName(mapOfMethods.get(methodFullName));
            }
            List<MethodCallExpr> callExprs = methodCallExpr.getChildNodes()
                    .stream()
                    .filter(node -> node instanceof MethodCallExpr)
                    .map(node -> {return (MethodCallExpr)node;})
                    .collect(Collectors.toList());
            callExprs.forEach(callExpr->{
                visit(callExpr, facade);
            });
        } catch (RuntimeException e) {
            System.out.println(methodCallExpr);
        }

    }
}
