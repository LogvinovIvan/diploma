package by.bsuir.fksis.poit.obfuscator.visitor.lexical.applier;

import by.bsuir.fksis.poit.obfuscator.state.lexical.ConvertedClassInf;
import by.bsuir.fksis.poit.obfuscator.util.lexical.LexicalVisitorPriority;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.AbstarctVisitor;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;


public class ApplierImportVisitor extends AbstarctVisitor {

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

    @Override
    public LexicalVisitorPriority getPriority() {
        return LexicalVisitorPriority.LOW;
    }
}
