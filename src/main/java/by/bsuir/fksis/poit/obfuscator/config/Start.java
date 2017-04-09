package by.bsuir.fksis.poit.obfuscator.config;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by Иван on 08.04.2017.
 */
public class Start {

    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        FileInputStream fileInputStream = new FileInputStream(classLoader.getResource("test.java").getFile());

        CompilationUnit cu = JavaParser.parse(fileInputStream);

        changeComment(cu.getChildNodes().get(1).getChildNodes());

        List<Comment> comments = cu.getComments();
        comments.forEach(comment -> System.out.println(comment instanceof JavadocComment));

        Comment comment = comments.get(0);
        boolean isLien = comment.isLineComment();
        boolean isOr = comment.isOrphan();

        //LineComment comment1 = (LineComment) comments.get(1);
        //Range range = comment1.getRange().orElse(null);
        //comment1.remove();
        //BlockComment blockComment = new BlockComment(range,comment1.getContent());
        //cu.addOrphanComment(blockComment);

        List<Node> nodes = cu.getChildNodes();


//        MethodVisitor methodVisitor = new MethodVisitor();


        PrettyPrinterConfiguration prettyPrinterConfiguration = new PrettyPrinterConfiguration();
        prettyPrinterConfiguration.setPrintComments(false);
        System.out.println(cu.toString(prettyPrinterConfiguration));
        Files.write(cu.toString(), new File("test1.java"), StandardCharsets.UTF_8);

        //System.out.println(cu.toString());

    }


    private static class JavaDocVisitor extends VoidVisitorAdapter<Object> {
        public void visit(JavadocComment comment, Object arg) {
            super.visit(comment, arg);
        }
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */
            System.out.println(n.getName());

            super.visit(n, arg);
        }
    }


    public static void changeComment(List<Node> childNodes) {
        for (Node node : childNodes) {
            if (node.hasComment()) {
                if (node.getComment().isLineComment()) {
                    BlockComment blockComment = new BlockComment(node.getComment().getRange().get(), node.getComment().getContent());
                    node.setBlockComment(blockComment.toString());
                    System.out.println(node);
                }
            }
        }
    }


//    private static class LineCommentVisitor extends VoidVisitorAdapter{
//        public void visit(LineComment comment, Object arg){
//            super.visit(comment,arg);
//            System.out.print(comment.toString());
//            Node node = comment.getParentNode().orElse(null);
//            comment.remove();
//            BlockComment blockComment = new BlockComment(comment.getRange().get(),comment.getContent());
//        }
//    }


}
