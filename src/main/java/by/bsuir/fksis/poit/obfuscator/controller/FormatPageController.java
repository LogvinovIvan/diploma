package by.bsuir.fksis.poit.obfuscator.controller;

import by.bsuir.fksis.poit.obfuscator.util.AbstractObfuscator;
import by.bsuir.fksis.poit.obfuscator.util.ObfuscatorComparator;
import by.bsuir.fksis.poit.obfuscator.util.syntax.CommentObfuscator;
import by.bsuir.fksis.poit.obfuscator.util.syntax.DebuggerInfObfuscator;
import by.bsuir.fksis.poit.obfuscator.util.syntax.OrderBlockObfuscator;
import by.bsuir.fksis.poit.obfuscator.util.syntax.SpaceObfuscator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by Иван on 09.04.2017.
 */
public class FormatPageController implements Initializable {

    private TreeSet<AbstractObfuscator> abstractObfuscatorPrioritySet = new TreeSet<>(new ObfuscatorComparator());
    private List<File> filesInFolder;
    private URL nextPageUrl;

    @FXML
    private Button buttonChoseFile = new Button();



    @FXML
    private CheckBox removeCommentCheckbox = new CheckBox();

    @FXML
    private CheckBox removeJavaDocCheckbox = new CheckBox();

    @FXML
    private CheckBox removeSpaceCheckbox = new CheckBox();

    @FXML
    private CheckBox changeOrderCheckBox = new CheckBox();

    @FXML
    private Button nextWindowButton = new Button();

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
            Connector.addObfuscator(new CommentObfuscator());
        } else {
            abstractObfuscatorPrioritySet.removeIf(obfuscator -> obfuscator instanceof CommentObfuscator);
        }

    }

    @FXML
    private void handleJavaDocCheckbox(ActionEvent event) {
        if (removeJavaDocCheckbox.isSelected()) {
            Connector.addObfuscator(new DebuggerInfObfuscator());
        } else {
            abstractObfuscatorPrioritySet.removeIf(obfuscator -> obfuscator instanceof DebuggerInfObfuscator);
        }

    }

    @FXML
    private void handleSpaceObfuscator(ActionEvent event) {
        if (removeSpaceCheckbox.isSelected()) {
            Connector.addObfuscator(new SpaceObfuscator());
        } else {
            abstractObfuscatorPrioritySet.removeIf(obfuscator -> obfuscator instanceof SpaceObfuscator);
        }
    }

//    @FXML
//    public void handleObfuscateButton(ActionEvent event) throws IOException {
//
//    }

    @FXML
    public void handleOrderObfuscateCheckBox(ActionEvent event) {
        if (changeOrderCheckBox.isSelected()) {
            Connector.addObfuscator(new OrderBlockObfuscator());
        } else {
            abstractObfuscatorPrioritySet.removeIf(obfuscator -> obfuscator instanceof OrderBlockObfuscator);
        }
    }



    @FXML
    public void handleNextButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(nextPageUrl);

        Stage currentStage = (Stage) nextWindowButton.getScene().getWindow();
        currentStage.close();

        Stage stage = new Stage();
        stage.setTitle("Formating obfuscate config ");
        stage.setScene(new Scene(root, 500, 500));
        stage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nextPageUrl = WindowManager.getInstance().getUrlWindow(this.getClass());
    }
}
