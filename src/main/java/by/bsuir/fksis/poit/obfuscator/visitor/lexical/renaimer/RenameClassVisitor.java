package by.bsuir.fksis.poit.obfuscator.visitor.lexical.renaimer;

import by.bsuir.fksis.poit.obfuscator.state.lexical.LexicalClassNameInf;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import org.apache.commons.lang.RandomStringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RenameClassVisitor extends VoidVisitorAdapter<JavaParserFacade> {

    private Map<String, String> mapOfRenaimedClasses = new HashMap<>();
    private HashMap<String, String> mapMethodsName = new HashMap<>();
    private HashMap<String, String> mapfieldsName = new HashMap<>();
    private List<LexicalClassNameInf> lexicalClassNameInfList;
    private LexicalClassNameInf lexicalClassNameInf;
    private String packageName;


    private final int lengthNameClasses = 20;
    private final int METHOD_LENGTH_NAME = 20;

    public RenameClassVisitor(List<LexicalClassNameInf> lexicalClassNameInfList) {
        this.lexicalClassNameInfList = lexicalClassNameInfList;

    }

    public void visit(PackageDeclaration packageDeclaration, JavaParserFacade javaParserFacade) {
        this.packageName = packageDeclaration.getNameAsString();
    }


    public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, JavaParserFacade javaParserFacade) {

        lexicalClassNameInf = new LexicalClassNameInf();
        lexicalClassNameInfList.add(lexicalClassNameInf);
        lexicalClassNameInf.setPackageName(packageName);
        lexicalClassNameInf.setFullName(javaParserFacade.getTypeDeclaration(classOrInterfaceDeclaration).getQualifiedName());

        String newClassName = RandomStringUtils.randomAlphabetic(lengthNameClasses);
        String oldName = classOrInterfaceDeclaration.getNameAsString();

        if (oldName.equals("BuilderOfCode")) {
            System.out.println(classOrInterfaceDeclaration);
        }

        lexicalClassNameInf.setOldClassName(oldName);
        lexicalClassNameInf.setNewClassName(newClassName);

//        classOrInterfaceDeclaration.setName(newClassName);
//        renameTypeField(classOrInterfaceDeclaration,lexicalClassNameInf);

        super.visit(classOrInterfaceDeclaration, javaParserFacade);
    }


//    @Override
//    public void visit(MethodDeclaration methodDeclaration, JavaParserFacade facade){
//        Type type = methodDeclaration.getType();
//        if(lexicalClassNameInf.getOldClassName().equals(type.toString())){
//            methodDeclaration.setType(lexicalClassNameInf.getNewClassName());
//        }
//        super.visit(methodDeclaration,facade);
//    }
//
//    public void visit(Parameter parameter, JavaParserFacade facade){
//        if(parameter.getType().toString().equals(lexicalClassNameInf.getOldClassName())){
//            parameter.setType(lexicalClassNameInf.getNewClassName());
//        }
//    }
//
//    @Override
//    public void visit(CastExpr castExpr, JavaParserFacade facade){
//        super.visit(castExpr,facade);
//        if(castExpr.getType().toString().equals(lexicalClassNameInf.getOldClassName())){
//            castExpr.setType(lexicalClassNameInf.getNewClassName());
//        }
//    }
//
//    @Override
//    public void visit(InstanceOfExpr expr, JavaParserFacade arg){
//        super.visit(expr,arg);
//        if(expr.getType().toString().equals(lexicalClassNameInf.getOldFullName())){
//            expr.setType(lexicalClassNameInf.getNewClassName());
//        }
//    }
//
//    @Override
//    public void visit(FieldDeclaration fieldDeclaration, JavaParserFacade parserFacade){
//        if(fieldDeclaration.getCommonType().toString().equals(lexicalClassNameInf.getOldClassName())){
//            fieldDeclaration.getVariable(0).setType(lexicalClassNameInf.getNewClassName());
//        }
//        super.visit(fieldDeclaration,parserFacade);
//    }
//
//
//
//    public void visit(VariableDeclarator declarator, JavaParserFacade facade){
//        if(declarator.getType().toString().equals(lexicalClassNameInf.getOldClassName())){
//            declarator.setType(lexicalClassNameInf.getNewClassName());
//        }
//        super.visit(declarator,facade);
//    }
//
//
//    public void visit(ConstructorDeclaration constructorDeclaration, JavaParserFacade javaParserFacade){
//        constructorDeclaration.setName(lexicalClassNameInf.getNewClassName());
//        super.visit(constructorDeclaration,javaParserFacade);
//    }
//
//
//    private void renameTypeField(ClassOrInterfaceDeclaration declaration, LexicalClassNameInf lexicalClassNameInf){
//        List<FieldDeclaration> fieldDeclarations = declaration.getFields();
//        for(FieldDeclaration fieldDeclaration: fieldDeclarations){
//            if(lexicalClassNameInf.getOldClassName().equals(fieldDeclaration.getCommonType().toString())){
//                List<VariableDeclarator> variableDeclarators = fieldDeclaration.getVariables();
//                variableDeclarators.forEach(v->v.setType(lexicalClassNameInf.getNewClassName()));
//            }
//        }
//    }


}
