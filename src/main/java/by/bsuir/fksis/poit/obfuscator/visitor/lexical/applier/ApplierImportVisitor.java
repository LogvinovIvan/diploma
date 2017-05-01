package by.bsuir.fksis.poit.obfuscator.visitor.lexical.applier;

import by.bsuir.fksis.poit.obfuscator.state.lexical.ConvertedClassInf;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;


public class ApplierImportVisitor extends VoidVisitorAdapter<JavaParserFacade> {

    private ConvertedClassInf convertedClassInf;

    public ApplierImportVisitor(ConvertedClassInf convertedClassInf) {
        this.convertedClassInf = convertedClassInf;
    }


    public void visit(ImportDeclaration importDeclaration, JavaParserFacade facade) {
        String currentImport = importDeclaration.getNameAsString();
        if (convertedClassInf.containsPackage(currentImport)) {
            importDeclaration.setName(convertedClassInf.getNewPackageName(currentImport));
        }
    }
}
