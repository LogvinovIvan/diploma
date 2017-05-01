package by.bsuir.fksis.poit.obfuscator.config;

import by.bsuir.fksis.poit.obfuscator.state.lexical.ConvertedClassInf;
import by.bsuir.fksis.poit.obfuscator.state.lexical.LexicalClassNameInf;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.applier.ApplierFieldVisitor;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.applier.ApplierMethodVisitor;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.renaimer.RenameClassVisitor;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.renaimer.RenameFieldVisitor;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.renaimer.RenameMethodVisitor;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Иван on 08.04.2017.
 */
public class Start {

    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        FileInputStream fileInputStream = new FileInputStream("B:\\epam\\test project\\epamJavaParser\\src\\by\\bsuir\\textparser\\composite\\CompositeElement.java");
        File src = new File("B:\\epam\\test project\\epamJavaParser\\src");
        TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(),
                new JavaParserTypeSolver(new File("B:\\epam\\test project\\epamJavaParser\\src")),
                new JarTypeSolver("B:\\epam\\test project\\epamJavaParser\\lib\\log4j-1.2.17.jar")
        );

        FileInputStream fileInputStream1 = new FileInputStream("B:\\epam\\test project\\epamJavaParser\\src\\by\\bsuir\\textparser\\parser\\TextParser.java");


        List<File> files = Files.walk(Paths.get(src.getAbsolutePath()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());


        List<LexicalClassNameInf> lexicalClassNameInfList = new ArrayList<>();
        files.stream().map(
                file -> {
                    List<LexicalClassNameInf> lexicalClassNameInfs = null;
                    try {
                        CompilationUnit compilationUnit = JavaParser.parse(file);
                        lexicalClassNameInfs = analyzeClassObfuscate(compilationUnit, JavaParserFacade.get(typeSolver));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return lexicalClassNameInfs;
                }
        ).forEach(lexicalClassNameInfs -> {
            lexicalClassNameInfList.addAll(lexicalClassNameInfs);
        });


        ConvertedClassInf convertedClassInf = new ConvertedClassInf(lexicalClassNameInfList);


        files.forEach(file -> {
            try {
                CompilationUnit compilationUnit = JavaParser.parse(file);
                applyChangesToCU(compilationUnit, JavaParserFacade.get(typeSolver), convertedClassInf);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(compilationUnit.toString().getBytes());
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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


//    public static void changeComment(List<Node> childNodes) {
//        for (Node node : childNodes) {
//            node.ha
//            if (node.hasComment()) {
//                if (node.getComment().) {
//                    BlockComment blockComment = new BlockComment(node.getComment().getRange().get(), node.getComment().getContent());
//                    node.setBlockComment(blockComment.toString());
//                    System.out.println(node);
//                }
//            }
//        }
//    }


//    private static class LineCommentVisitor extends VoidVisitorAdapter{
//        public void visit(LineComment comment, Object arg){
//            super.visit(comment,arg);
//            System.out.print(comment.toString());
//            Node node = comment.getParentNode().orElse(null);
//            comment.remove();
//            BlockComment blockComment = new BlockComment(comment.getRange().get(),comment.getContent());
//        }
//    }


    private static List<LexicalClassNameInf> analyzeClassObfuscate(CompilationUnit compilationUnit, JavaParserFacade facade) {
        LexicalClassNameInf lexicalClassNameInf = new LexicalClassNameInf();

        List<LexicalClassNameInf> lexicalClassNameInfList = new ArrayList<>();

        RenameClassVisitor renameClassVisitor = new RenameClassVisitor(lexicalClassNameInfList);
        compilationUnit.accept(renameClassVisitor, facade);

        RenameMethodVisitor renameMethodVisitor = new RenameMethodVisitor(lexicalClassNameInfList);
        compilationUnit.accept(renameMethodVisitor, facade);

        RenameFieldVisitor renameFieldVisitor = new RenameFieldVisitor(lexicalClassNameInfList);
        compilationUnit.accept(renameFieldVisitor, facade);

        return lexicalClassNameInfList;
    }


    private static void applyChangesToCU(CompilationUnit compilationUnit, JavaParserFacade facade, ConvertedClassInf convertedClassInf) {


        ApplierMethodVisitor applierMethodVisitor = new ApplierMethodVisitor((HashMap<String, String>) convertedClassInf.getMapSimpleNameMethod());
        compilationUnit.accept(applierMethodVisitor, facade);

        ApplierFieldVisitor applierFieldVisitor = new ApplierFieldVisitor(convertedClassInf);
        compilationUnit.accept(applierFieldVisitor, facade);

//        ApplierRenameClassVisitor applierRenameClassVisitor = new ApplierRenameClassVisitor(convertedClassInf);
//        compilationUnit.accept(applierRenameClassVisitor, facade);


    }


}
