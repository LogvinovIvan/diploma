package by.bsuir.fksis.poit.obfuscator.visitor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Иван on 15.04.2017.
 */
public class TestMethodCallResolverVisitor extends VoidVisitorAdapter<JavaParserFacade> {
    @Override
    public void visit(ReturnStmt n, JavaParserFacade javaParserFacade) {
        super.visit(n, javaParserFacade);
        //System.out.println(n.getExpr().toString() + " has type " + javaParserFacade.getType(n.getExpr()));
    }


//    @Override
//    public void visit(MethodCallExpr n, JavaParserFacade javaParserFacade) {
//        super.visit(n, javaParserFacade);
//        System.out.println(n.toString() + " has type " + javaParserFacade.ge);
//        if (javaParserFacade.getType(n).isReferenceType()) {
//            for (ReferenceType ancestor : javaParserFacade.getType(n).asReferenceType().getAllAncestors()) {
//                //System.out.println("Ancestor " + ancestor.describe());
//            }
//        }
//    }


//    public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, JavaParserFacade javaParserFacade){
//        Set<MethodUsage> methodUsages = javaParserFacade.getType(classOrInterfaceDeclaration).replaceTypeVariables(new TypeParameterDeclaration() {
//        });
//    }

    public void visit(MethodCallExpr fieldDeclaration, JavaParserFacade javaParserFacade) {
        javaParserFacade.solve(fieldDeclaration).getCorrespondingDeclaration().getQualifiedSignature();
    }

    public void visit(FieldDeclaration fieldDeclaration, JavaParserFacade javaParserFacade) {
        System.out.println(javaParserFacade.getType(fieldDeclaration.getVariable(0)));
    }


    public static void main(String[] args) throws FileNotFoundException {


        TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(), new JavaParserTypeSolver(new File("B:\\3 курс\\весенний семестр\\СПП\\car\\carShop\\src")), new JavaParserTypeSolver(new File("B:\\3 курс\\весенний семестр\\СПП\\car\\carShop\\target\\classes")));

        CompilationUnit agendaCu = JavaParser.parse(new FileInputStream(new File("B:\\3 курс\\весенний семестр\\СПП\\car\\carShop\\src\\main\\java\\entity\\Car.java")));
//        Navigator.findAllNodesOfGivenClass()


        agendaCu.accept(new TestMethodCallResolverVisitor(), JavaParserFacade.get(typeSolver));
    }

}
