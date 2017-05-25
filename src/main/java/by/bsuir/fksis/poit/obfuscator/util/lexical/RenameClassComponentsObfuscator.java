package by.bsuir.fksis.poit.obfuscator.util.lexical;

import by.bsuir.fksis.poit.obfuscator.config.PriorityObfuscatorLevel;
import by.bsuir.fksis.poit.obfuscator.state.StateClass;
import by.bsuir.fksis.poit.obfuscator.state.lexical.ConvertedClassInf;
import by.bsuir.fksis.poit.obfuscator.state.lexical.LexicalClassNameInf;
import by.bsuir.fksis.poit.obfuscator.util.AbstractObfuscator;
import by.bsuir.fksis.poit.obfuscator.util.lexical.factory.LexicalFactory;
import by.bsuir.fksis.poit.obfuscator.visitor.lexical.AbstarctVisitor;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Иван on 09.05.2017.
 */
public class RenameClassComponentsObfuscator extends AbstractObfuscator {

    {
        this.level = PriorityObfuscatorLevel.SUPER_HIGH;
    }

    private TreeSet<AbstarctVisitor> renameAdapters = new TreeSet<>();
    private JavaParserFacade facade;
    private TreeSet<LexicalFactory> factories = new TreeSet<>();
    private List<LexicalClassNameInf> lexicalClassNameInfList = new ArrayList<>();
    private ConvertedClassInf convertedClassInf;


    private String src;
    private String libs;


    public RenameClassComponentsObfuscator(String src, String libs, TreeSet<LexicalFactory> lexicalFactories) throws IOException {
        this.src = src;
        this.libs = libs;
        this.factories = lexicalFactories;
        factories.forEach(lexicalFactory -> {
            List<VoidVisitorAdapter> adapters = lexicalFactory.createRenameVisitor(lexicalClassNameInfList);
            renameAdapters.addAll(adapters.stream().map(a -> (AbstarctVisitor) a).collect(Collectors.toSet()));
        });
        this.convertedClassInf = buildMapOfDependies();
    }


    @Override
    public void obfuscate(StateClass stateClass) {
        CompilationUnit compilationUnit = stateClass.getCompilationUnit();
        applyChangesToCU(compilationUnit, this.facade, this.convertedClassInf);
    }


    private List<LexicalClassNameInf> analyzeClassObfuscate(CompilationUnit compilationUnit, JavaParserFacade facade) {

        List<LexicalClassNameInf> lexicalClassNameInfList = new ArrayList<>();

        List<VoidVisitorAdapter> adapters = new ArrayList<>();
        factories.forEach(
                lexicalFactory -> adapters.addAll(lexicalFactory.createRenameVisitor(lexicalClassNameInfList))
        );
        adapters.forEach(
                voidVisitorAdapter -> compilationUnit.accept(voidVisitorAdapter, facade)
        );
        return lexicalClassNameInfList;
    }


    private void applyChangesToCU(CompilationUnit compilationUnit, JavaParserFacade facade, ConvertedClassInf convertedClassInf) {

        List<VoidVisitorAdapter> adapters = new LinkedList<>();
        factories.forEach(lexicalFactory ->
                adapters.addAll(lexicalFactory.createApplierVisitor(convertedClassInf))
        );

        List<AbstarctVisitor> abstarctVisitors = adapters.stream().map(a -> (AbstarctVisitor) a).collect(Collectors.toList());
        Collections.sort(abstarctVisitors);
        Collections.sort(abstarctVisitors, Collections.reverseOrder());

        abstarctVisitors.forEach(voidVisitorAdapter -> compilationUnit.accept(voidVisitorAdapter, facade));
    }

    private ConvertedClassInf buildMapOfDependies() throws IOException {
        File src = new File(this.src);
        CombinedTypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(),
                new JavaParserTypeSolver(new File(this.src))
        );
        addJarTypeSolver(this.libs, typeSolver);

        this.facade = JavaParserFacade.get(typeSolver);


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
        return new ConvertedClassInf(lexicalClassNameInfList);
    }

    private void addJarTypeSolver(String pathLib, CombinedTypeSolver typeSolver) throws IOException {
        File libPath = new File(pathLib);
        List<String> libs = Files.walk(Paths.get(libPath.getAbsolutePath()))
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .collect(Collectors.toList());
        libs.forEach(
                l -> {
                    try {
                        JarTypeSolver jarTypeSolver = new JarTypeSolver(l);
                        typeSolver.add(jarTypeSolver);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }


}
