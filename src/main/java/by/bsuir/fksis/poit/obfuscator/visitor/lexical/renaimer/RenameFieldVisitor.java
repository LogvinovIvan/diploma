package by.bsuir.fksis.poit.obfuscator.visitor.lexical.renaimer;

import by.bsuir.fksis.poit.obfuscator.state.lexical.LexicalClassNameInf;
import by.bsuir.fksis.poit.obfuscator.util.lexical.LexicalVisitorPriority;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.AbstarctVisitor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import org.apache.commons.lang.RandomStringUtils;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by Иван on 15.04.2017.
 */
public class RenameFieldVisitor extends AbstarctVisitor {

    private Integer LENGTH_NAME_FIELD = 15;
    private int countClass = 0;

    private LexicalClassNameInf lexicalClassNameInf;
    private List<LexicalClassNameInf> lexicalClassNameInfList;

    public RenameFieldVisitor(List<LexicalClassNameInf> lexicalClassNameInfList) {
        this.lexicalClassNameInfList = lexicalClassNameInfList;
    }

    public void visit(ClassOrInterfaceDeclaration declaration, JavaParserFacade facade) {
        lexicalClassNameInf = lexicalClassNameInfList.get(countClass);
        countClass++;
        super.visit(declaration, facade);
    }

    @Override
    public void visit(FieldDeclaration fieldDeclaration, JavaParserFacade facade) {
        EnumSet<Modifier> modifiers = fieldDeclaration.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE)) {
            fieldDeclaration.getVariables()
                    .forEach(variable -> {
                        String oldName = variable.getNameAsString();
                        String newFiledName = RandomStringUtils.randomAlphabetic(LENGTH_NAME_FIELD);
                        lexicalClassNameInf.addField(newFiledName, oldName);
                    });
        }
        super.visit(fieldDeclaration, facade);
    }

    @Override
    public LexicalVisitorPriority getPriority() {
        return LexicalVisitorPriority.LOW;
    }
}
