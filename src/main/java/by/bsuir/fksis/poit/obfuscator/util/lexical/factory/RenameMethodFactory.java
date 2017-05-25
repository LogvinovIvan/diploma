package by.bsuir.fksis.poit.obfuscator.util.lexical.factory;

import by.bsuir.fksis.poit.obfuscator.state.lexical.ConvertedClassInf;
import by.bsuir.fksis.poit.obfuscator.state.lexical.LexicalClassNameInf;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.applier.ApplierMethodVisitor;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.renaimer.RenameMethodVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Иван on 09.05.2017.
 */
public class RenameMethodFactory extends LexicalFactory {

    public RenameMethodFactory() {
        priority = Priority.HIGH;
    }

    @Override
    public List<VoidVisitorAdapter> createRenameVisitor(List<LexicalClassNameInf> lexicalClassNameInfList) {
        List<VoidVisitorAdapter> list = new ArrayList<>();
        list.add(new RenameMethodVisitor(lexicalClassNameInfList));
        return list;
    }

    @Override
    public List<VoidVisitorAdapter> createApplierVisitor(ConvertedClassInf convertedClassInf) {
        List<VoidVisitorAdapter> list = new ArrayList<>();
        list.add(new ApplierMethodVisitor((HashMap<String, String>) convertedClassInf.getMapSimpleNameMethod(), convertedClassInf.getStaticMethodMarkerMap(), convertedClassInf));
        return list;
    }
}
