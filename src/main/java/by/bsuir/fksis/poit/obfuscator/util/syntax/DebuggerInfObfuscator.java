package by.bsuir.fksis.poit.obfuscator.util.syntax;

import by.bsuir.fksis.poit.obfuscator.config.PriorityObfuscatorLevel;
import by.bsuir.fksis.poit.obfuscator.state.StateClass;
import by.bsuir.fksis.poit.obfuscator.util.AbstractObfuscator;
import by.bsuir.fksis.poit.obfuscator.visitor.syntax.RemoveJavaDocVisitor;

/**
 * Created by Иван on 08.04.2017.
 */
public class DebuggerInfObfuscator extends AbstractObfuscator {

    private RemoveJavaDocVisitor removeJavaDocVisitor;

    {
        level = PriorityObfuscatorLevel.LOW;
    }

    public DebuggerInfObfuscator() {
        this.removeJavaDocVisitor = new RemoveJavaDocVisitor();
    }

    public DebuggerInfObfuscator(RemoveJavaDocVisitor removeJavaDocVisitor) {
        this.removeJavaDocVisitor = removeJavaDocVisitor;
    }

    public void obfuscate(StateClass stateClass) {
        removeJavaDocVisitor.visit(stateClass.getCompilationUnit(), null);
        if (next != null) {
            next.obfuscate(stateClass);
        }
    }
}
