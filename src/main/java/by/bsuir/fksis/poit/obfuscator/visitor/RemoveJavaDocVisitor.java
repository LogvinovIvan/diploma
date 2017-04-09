package by.bsuir.fksis.poit.obfuscator.visitor;

import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Created by Иван on 09.04.2017.
 */
public class RemoveJavaDocVisitor extends VoidVisitorAdapter {

    public void visit(JavadocComment javadocComment, Object arg) {
        javadocComment.remove();
    }
}
