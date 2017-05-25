package by.bsuir.fksis.poit.obfuscator.controller;


import by.bsuir.fksis.poit.obfuscator.dao.FileDao;
import com.github.javaparser.JavaParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Иван on 21.05.2017.
 */
public class FileController implements Initializable {

    @FXML
    public Label statusText = new Label();
    @FXML
    public Label fileCount = new Label();
    public TextArea listFiles;


    public List<String> fileName = new ArrayList<>();

    public List<String> badFiles = new ArrayList<>();


    private boolean isBadFiles = false;

    public FileController() {


    }


    public void handleNextButton(ActionEvent event) throws IOException {
        if (isBadFiles) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/start.fxml"));

            Stage currentStage = (Stage) statusText.getScene().getWindow();
            currentStage.close();

            Stage stage = new Stage();
            stage.setTitle("Formating obfuscate config ");
            stage.setScene(new Scene(root, 340, 340));
            stage.show();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/FormatingObfuscateWindow.fxml"));

            Stage currentStage = (Stage) statusText.getScene().getWindow();
            currentStage.close();

            Stage stage = new Stage();
            stage.setTitle("Formating obfuscate config ");
            stage.setScene(new Scene(root, 500, 500));
            stage.show();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FileDao dao = new FileDao();
        List<File> files = dao.getSrcFiles(Connector.getSrc());

        for (File file : files) {
            try {
                JavaParser.parse(file);
                fileName.add(file.getName());
            } catch (Exception e) {
                isBadFiles = true;
                badFiles.add(file.getName());
            }
        }

        if (isBadFiles) {
            listFiles.appendText(String.join("\n", badFiles));
            fileCount.setText(Integer.toString(badFiles.size()));
            statusText.setText("Bad files:");
        } else {
            listFiles.appendText(String.join("\n", fileName));
            fileCount.setText(Integer.toString(files.size()));
            statusText.setText("Total files count:");
        }
    }


}
