package by.bsuir.fksis.poit.obfuscator.state;

import com.github.javaparser.ast.CompilationUnit;

/**
 * Created by Иван on 09.04.2017.
 */
public class StateClass {
    private CompilationUnit compilationUnit;
    private String content = null;


    public StateClass() {
    }

    public StateClass(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }


    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public void setCompilationUnit(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    public String getContent() {
        if (content == null) {
            content = compilationUnit.toString();
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
