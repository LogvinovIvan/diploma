package by.bsuir.fksis.poit.obfuscator.visitor.lexical;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

/**
 * Created by Иван on 21.05.2017.
 */
public abstract class AbstarctVisitor extends VoidVisitorAdapter<JavaParserFacade> implements Prioritable, Comparable<AbstarctVisitor> {


    @Override
    public int compareTo(AbstarctVisitor o) {
        return Integer.compare(this.getPriority().getPriority(), o.getPriority().getPriority());
    }
}
