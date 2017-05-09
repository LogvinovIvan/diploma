package by.bsuir.fksis.poit.obfuscator.util.lexical.factory;

import by.bsuir.fksis.poit.obfuscator.state.lexical.ConvertedClassInf;
import by.bsuir.fksis.poit.obfuscator.state.lexical.LexicalClassNameInf;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.applier.ApplierImportVisitor;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.applier.ApplierRenameClassVisitor;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.renaimer.RenameClassVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Иван on 09.05.2017.
 */
public class RenameClassFactory extends LexicalFactory {
    @Override
    public List<VoidVisitorAdapter> createRenameVisitor(List<LexicalClassNameInf> lexicalClassNameInfList) {
        List<VoidVisitorAdapter> renameList = new ArrayList<>();
        renameList.add(new RenameClassVisitor(lexicalClassNameInfList));
        return renameList;
    }

    @Override
    public List<VoidVisitorAdapter> createApplierVisitor(ConvertedClassInf convertedClassInf) {
        List<VoidVisitorAdapter> applierVisitors = new ArrayList<>();
        applierVisitors.add(new ApplierRenameClassVisitor(convertedClassInf));
        applierVisitors.add(new ApplierImportVisitor(convertedClassInf));
        return applierVisitors;
    }
}
