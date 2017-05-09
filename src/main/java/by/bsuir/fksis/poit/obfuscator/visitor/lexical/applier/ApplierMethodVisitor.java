package by.bsuir.fksis.poit.obfuscator.visitor.lexical.applier;

import by.bsuir.fksis.poit.obfuscator.state.lexical.ConvertedClassInf;
import by.bsuir.fksis.poit.obfuscator.util.comparator.MethodUsageComparator;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
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
    private Map<String, Boolean> staticMethodMap;
    private TreeSet<MethodUsage> methodUsages = new TreeSet<>(new MethodUsageComparator());
    private Set<String> methodsOfClassObject = new HashSet<>();
    private static MethodUsageComparator comparator = new MethodUsageComparator();
    private ConvertedClassInf convertedClassInf;

    {
        methodsOfClassObject.add("clone");
        methodsOfClassObject.add("toString");
        methodsOfClassObject.add("equals");
        methodsOfClassObject.add("hashCode");
        methodsOfClassObject.add("finalize");
    }


    public ApplierMethodVisitor(HashMap<String, String> mapOfMethods, Map<String, Boolean> staticMethodMap, ConvertedClassInf convertedClassInf) {
        this.mapOfMethods = mapOfMethods;
        this.staticMethodMap = staticMethodMap;
        this.convertedClassInf = convertedClassInf;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, JavaParserFacade javaParserFacade) {

        if (classOrInterfaceDeclaration.getNameAsString().equals("BuilderOfCode")) {
            System.out.println("asasa");
        }

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

            if (containseMethodDeclaration(usage)) {
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

            if (!containseMethodDeclaration(usage)) {
                String newMethodName = mapOfMethods.get(usage.getQualifiedSignature());
                if (newMethodName != null) {
                    methodDeclaration.setName(newMethodName);
                }
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
            renameScope(methodCallExpr, methodFullName, facade);
        } catch (RuntimeException e) {
            System.out.println(methodCallExpr);
        }

        super.visit(methodCallExpr, facade);

    }


    public void visit(LambdaExpr lambdaExpr, JavaParserFacade facade) {
        List<MethodCallExpr> exprs = lambdaExpr.getNodesByType(MethodCallExpr.class);
        exprs.forEach(
                methodCallExpr -> {
                    visit(methodCallExpr, facade);
                }
        );
        List<MethodReferenceExpr> referenceExprs = lambdaExpr.getNodesByType(MethodReferenceExpr.class);
        super.visit(lambdaExpr, facade);
    }


    //    public void visit(MethodReferenceExpr expr, JavaParserFacade facade){
//        String scope = expr.getScope().toString();
//        String[] parts = scope.split("::");
//        String clazz = parts[0];
//        if(clazz.equals("this")){
//            facade.getSymbolSolver().solveSymbol(expr.getIdentifier(),expr);
//        }
//        super.visit(expr,facade);
//    }
    private boolean containseMethodDeclaration(MethodUsage usage) {
        return methodUsages.stream().filter(methodUsage -> comparator.compare(methodUsage, usage) == 0).collect(Collectors.toList()).size() > 0;
    }


    private void renameScope(MethodCallExpr expr, String signature, JavaParserFacade facade) {
        if (staticMethodMap.containsKey(signature) && staticMethodMap.get(signature)) {
            Optional<Expression> scopeExpr = expr.getScope();
            if (scopeExpr.isPresent()) {
                String scope = scopeExpr.get().toString();
                if (!"this".equals(scope) && !scope.isEmpty()) {
                    String fullType = facade.getSymbolSolver().solveType(scope, expr).getCorrespondingDeclaration().getQualifiedName();
                    String[] partsOfScope = scope.split(".");
                    String simpleName;
                    if (partsOfScope.length == 0) {
                        simpleName = scope;
                        partsOfScope = new String[1];
                    } else {
                        simpleName = partsOfScope[0];
                    }

                    String newClassName = defineNewClassName(fullType, simpleName);
                    partsOfScope[0] = newClassName;
                    StringBuilder newScope = new StringBuilder();
                    if (partsOfScope.length > 1) {
                        newScope.append(partsOfScope[0]);
                        for (int i = 1; i < partsOfScope.length; i++) {
                            newScope.append(".").append(partsOfScope[i]);
                        }
                    } else {
                        newScope.append(newClassName);
                    }
                    NameExpr nameExpr = ((NameExpr) scopeExpr.get()).setName(newScope.toString());
                    expr.setScope(nameExpr);
                }
            }

        }
    }

    private String defineNewClassName(String oldClassName, String currenName) {
        String newClassName;
        if (!oldClassName.equals(currenName)) {
            newClassName = convertedClassInf.getNewClassName(oldClassName);
        } else {
            newClassName = convertedClassInf.getNewFullClassName(oldClassName);
        }
        return newClassName;
    }




}
