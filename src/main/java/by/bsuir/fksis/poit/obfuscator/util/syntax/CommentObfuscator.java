package by.bsuir.fksis.poit.obfuscator.util.syntax;

import by.bsuir.fksis.poit.obfuscator.config.PriorityObfuscatorLevel;
import by.bsuir.fksis.poit.obfuscator.state.StateClass;
import by.bsuir.fksis.poit.obfuscator.util.AbstractObfuscator;
import by.bsuir.fksis.poit.obfuscator.visitor.RemoveCommentVisitor;

/**
 * Created by Иван on 08.04.2017.
 */
public class CommentObfuscator extends AbstractObfuscator {

    private RemoveCommentVisitor removeCommentVisitor;

    {
        level = PriorityObfuscatorLevel.MIDDLE;
    }

    public CommentObfuscator() {
        removeCommentVisitor = new RemoveCommentVisitor();
    }

    public CommentObfuscator(RemoveCommentVisitor removeCommentVisitor) {
        this.removeCommentVisitor = removeCommentVisitor;
    }

    public void obfuscate(StateClass stateClass) {
        removeCommentVisitor.visit(stateClass.getCompilationUnit(), null);
        next.obfuscate(stateClass);
    }
}
