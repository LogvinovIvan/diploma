package by.bsuir.fksis.poit.obfuscator.controller;

import by.bsuir.fksis.poit.obfuscator.state.StateClass;
import by.bsuir.fksis.poit.obfuscator.util.AbstractObfuscator;
import by.bsuir.fksis.poit.obfuscator.util.ObfuscatorComparator;
import by.bsuir.fksis.poit.obfuscator.util.syntax.CommentObfuscator;
import by.bsuir.fksis.poit.obfuscator.util.syntax.DebuggerInfObfuscator;
import by.bsuir.fksis.poit.obfuscator.util.syntax.SpaceObfuscator;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by Иван on 09.04.2017.
 */
public class SyntaxPageController {

    private TreeSet<AbstractObfuscator> abstractObfuscatorPriorityQueue = new TreeSet<>(new ObfuscatorComparator());
    private List<File> filesInFolder;

    @FXML
    private Button buttonChoseFile = new Button();



    @FXML
    private CheckBox removeCommentCheckbox = new CheckBox();

    @FXML
    private CheckBox removeJavaDocCheckbox = new CheckBox();

    @FXML
    private CheckBox removeSpaceCheckbox = new CheckBox();


    @FXML
    private void handlebuttonChooseFile(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(new Stage());
        System.out.print(file.getAbsolutePath());


        try {
            filesInFolder = Files.walk(Paths.get(file.getAbsolutePath()))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            System.out.print(filesInFolder.size());
        } catch (IOException e) {

        }


    }

    @FXML
    private void handleRemoveComment(ActionEvent event) {
        if (removeCommentCheckbox.isSelected()) {
            abstractObfuscatorPriorityQueue.add(new CommentObfuscator());
        }

    }

    @FXML
    private void handleJavaDocCheckbox(ActionEvent event) {
        if (removeJavaDocCheckbox.isSelected()) {
            abstractObfuscatorPriorityQueue.add(new DebuggerInfObfuscator());
        }

    }

    @FXML
    private void handleSpaceObfuscator(ActionEvent event) {
        if (removeSpaceCheckbox.isSelected()) {
            abstractObfuscatorPriorityQueue.add(new SpaceObfuscator());
        }
    }

    @FXML
    public void handleObfuscateButton(ActionEvent event) throws IOException {
        AbstractObfuscator obfuscator = createChain();
        for (File file : filesInFolder) {
            CompilationUnit cu = JavaParser.parse(file);
            StateClass stateClass = new StateClass(cu);
            obfuscator.obfuscate(stateClass);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(stateClass.getContent().getBytes());
            fileOutputStream.close();
        }
    }

    private AbstractObfuscator createChain() {
        AbstractObfuscator result = abstractObfuscatorPriorityQueue.pollFirst();
        AbstractObfuscator prev = result;
        for (AbstractObfuscator abstractObfuscator : abstractObfuscatorPriorityQueue) {
            prev.setNext(abstractObfuscator);
            prev = abstractObfuscator;
        }

        return result;
    }


}
