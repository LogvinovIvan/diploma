package by.bsuir.fksis.poit.obfuscator.visitor.syntax;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Created by Иван on 09.04.2017.
 */
public class RemoveCommentVisitor extends VoidVisitorAdapter {

    @Override
    public void visit(LineComment lineComment, Object arg) {
        super.visit(lineComment, arg);
        lineComment.remove();
    }

    @Override
    public void visit(BlockComment blockComment, Object arg) {
        super.visit(blockComment, arg);
        blockComment.remove();
    }
}
