package by.bsuir.fksis.poit.obfuscator.visitor.lexical.renaimer;

import by.bsuir.fksis.poit.obfuscator.state.lexical.LexicalClassNameInf;
import by.bsuir.fksis.poit.obfuscator.util.comparator.MethodUsageComparator;
import by.bsuir.fksis.poit.obfuscator.util.lexical.LexicalVisitorPriority;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.AbstarctVisitor;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.methods.MethodUsage;
import com.github.javaparser.symbolsolver.model.typesystem.Type;
import org.apache.commons.lang.RandomStringUtils;

import java.util.*;


public class RenameMethodVisitor extends AbstarctVisitor {

    private final int METHOD_LENGTH_NAME = 20;

    private HashMap<String, String> mapMethodsName = new HashMap<>();
    private Set<MethodUsage> methodUsages = new TreeSet<>(new MethodUsageComparator());
    private LexicalClassNameInf lexicalClassNameInf;
    private List<LexicalClassNameInf> lexicalClassNameInfList;
    private int currentClass = 0;
    private Set<String> methodsOfClassObject = new HashSet<>();

    {
        methodsOfClassObject.add("clone");
        methodsOfClassObject.add("toString");
        methodsOfClassObject.add("equals");
        methodsOfClassObject.add("hashCode");
        methodsOfClassObject.add("finalize");
    }


    public RenameMethodVisitor(List<LexicalClassNameInf> lexicalClassNameInfList) {
        this.lexicalClassNameInfList = lexicalClassNameInfList;

    }


    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, JavaParserFacade javaParserFacade) {
        lexicalClassNameInf = lexicalClassNameInfList.get(currentClass);
        currentClass++;

        List<ClassOrInterfaceType> extentsTypes = classOrInterfaceDeclaration.getExtendedTypes();
        for (ClassOrInterfaceType extendType : extentsTypes) {
            methodUsages.addAll(javaParserFacade.convertToUsage(extendType).asReferenceType().getDeclaredMethods());
        }

        List<ClassOrInterfaceType> implementedInterfaces = classOrInterfaceDeclaration.getImplementedTypes();
        for (ClassOrInterfaceType implementedInterface : implementedInterfaces) {
            methodUsages.addAll(javaParserFacade.convertToUsage(implementedInterface).asReferenceType().getDeclaredMethods());
        }

        super.visit(classOrInterfaceDeclaration, javaParserFacade);
    }


    @Override
    public void visit(MethodDeclaration methodDeclaration, JavaParserFacade facade) {

        if (!methodDeclaration.isAnnotationPresent(Override.class) && !methodsOfClassObject.contains(methodDeclaration.getNameAsString())) {

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
                String oldSignature = usage.getQualifiedSignature();
                String newMethodName = RandomStringUtils.randomAlphabetic(METHOD_LENGTH_NAME);
                lexicalClassNameInf.addMethods(oldSignature, newMethodName);
                lexicalClassNameInf.setStaticMarkerForMethod(oldSignature, methodDeclaration.isStatic());
            } else {
                MethodUsage parent = methodUsages.stream()
                        .filter(methodUsage -> new MethodUsageComparator().compare(methodUsage, usage) == 0).findFirst().get();
                String parentSignature = "override:" + parent.getQualifiedSignature();
                String oldSignature = usage.getQualifiedSignature();
                lexicalClassNameInf.addMethods(oldSignature, parentSignature);
                lexicalClassNameInf.setStaticMarkerForMethod(oldSignature, methodDeclaration.isStatic());
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
            MethodUsage parent = methodUsages.stream()
                    .filter(methodUsage -> new MethodUsageComparator().compare(methodUsage, usage) == 0).findFirst().get();
            String parentSignature = "override:" + parent.getQualifiedSignature();
            String oldSignature = usage.getQualifiedSignature();
            lexicalClassNameInf.addMethods(oldSignature, parentSignature);
            lexicalClassNameInf.setStaticMarkerForMethod(oldSignature, methodDeclaration.isStatic());

        }

    }

    @Override
    public LexicalVisitorPriority getPriority() {
        return LexicalVisitorPriority.MID;
    }
}
