package by.bsuir.fksis.poit.obfuscator.util.lexical.factory;

import by.bsuir.fksis.poit.obfuscator.state.lexical.ConvertedClassInf;
import by.bsuir.fksis.poit.obfuscator.state.lexical.LexicalClassNameInf;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.applier.ApplierFieldVisitor;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.renaimer.RenameFieldVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Иван on 09.05.2017.
 */
public class RenameFieldFactory extends LexicalFactory implements Comparable<LexicalFactory> {
    public RenameFieldFactory() {
        priority = Priority.MID;
    }

    @Override
    public List<VoidVisitorAdapter> createRenameVisitor(List<LexicalClassNameInf> lexicalClassNameInfList) {
        List<VoidVisitorAdapter> voidVisitorAdapters = new ArrayList<>();
        voidVisitorAdapters.add(new RenameFieldVisitor(lexicalClassNameInfList));
        return voidVisitorAdapters;
    }

    @Override
    public List<VoidVisitorAdapter> createApplierVisitor(ConvertedClassInf convertedClassInf) {
        List<VoidVisitorAdapter> voidVisitorAdapters = new ArrayList<>();
        voidVisitorAdapters.add(new ApplierFieldVisitor(convertedClassInf));
        return voidVisitorAdapters;
    }


}
