package by.bsuir.fksis.poit.obfuscator.util.lexical.factory;

import by.bsuir.fksis.poit.obfuscator.state.lexical.ConvertedClassInf;
import by.bsuir.fksis.poit.obfuscator.state.lexical.LexicalClassNameInf;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * Created by Иван on 09.05.2017.
 */
public abstract class LexicalFactory implements Comparable<LexicalFactory> {
    protected Priority priority;

    public abstract List<VoidVisitorAdapter> createRenameVisitor(List<LexicalClassNameInf> lexicalClassNameInfList);

    public abstract List<VoidVisitorAdapter> createApplierVisitor(ConvertedClassInf convertedClassInf);

    enum Priority {
        LOW(0), MID(1), HIGH(2), SUPERHIGH(3);

        int priority;

        Priority(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

    }

    @Override
    public int compareTo(LexicalFactory o) {
        return Integer.compare(this.priority.getPriority(), o.priority.getPriority());
    }
}
