package by.bsuir.fksis.poit.obfuscator.controller;

import by.bsuir.fksis.poit.obfuscator.dao.FileDao;
import by.bsuir.fksis.poit.obfuscator.state.StateClass;
import by.bsuir.fksis.poit.obfuscator.util.AbstractObfuscator;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import javafx.concurrent.Task;

import java.io.File;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Иван on 21.05.2017.
 */
public class ObfuscationTask extends Task<List<File>> {

    FileDao dao = new FileDao();


    @Override
    protected List<File> call() throws Exception {
        List<File> srcFiles = dao.getSrcFiles(Connector.getSrc());

        try {
            AbstractObfuscator obfuscator = createChain();
            Integer totalFilesCount = srcFiles.size();
            Integer currentFileIndex = 0;
            for (File file : srcFiles) {
                CompilationUnit cu = JavaParser.parse(file);
                StateClass stateClass = new StateClass(cu);
                obfuscator.obfuscate(stateClass);
                dao.saveFileInRequiredPackage(stateClass, Connector.getSave());
                currentFileIndex++;
                this.updateProgress(currentFileIndex, totalFilesCount);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return srcFiles;
    }


    private AbstractObfuscator createChain() {
        TreeSet<AbstractObfuscator> obfuscators = (TreeSet<AbstractObfuscator>) Connector.getObfuscators().clone();
        AbstractObfuscator result = obfuscators.pollFirst();
        AbstractObfuscator prev = result;
        for (AbstractObfuscator abstractObfuscator : Connector.getObfuscators()) {
            prev.setNext(abstractObfuscator);
            prev = abstractObfuscator;
        }

        return result;
    }

}
