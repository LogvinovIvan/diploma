package by.bsuir.fksis.poit.obfuscator.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Иван on 09.05.2017.
 */
public class FileChooserController implements FilterController {

    private String srcPath;
    private String libPath;
    private String savePath;

    public FileChooserController() throws IOException {

    }

    @FXML
    private Button buttonChoseSrcFile = new Button();

    @FXML
    private Button buttonChooseLibFile = new Button();

    @FXML
    private Button buttonChooseSavePath = new Button();

    @FXML
    private CheckBox checkDefaultSaveDir = new CheckBox();

    @FXML
    private TextField srcPathText = new TextField();

    @FXML
    private TextField libPathText = new TextField();

    @FXML
    private TextField savePathText = new TextField();


    @FXML
    private void handleButtonChooseSrcPath(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(new Stage());
        srcPath = file.getAbsolutePath();
        srcPathText.setText(srcPath);
        srcPathText.setDisable(true);
        Connector.setSrc(srcPath);
    }


    @FXML
    private void handleButtonLibPath(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(new Stage());
        libPath = file.getAbsolutePath();
        libPathText.setText(libPath);
        Connector.setLib(libPath);
    }

    @FXML
    private void handleButtonSavePath(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(new Stage());
        savePath = file.getAbsolutePath();
        savePathText.setText(savePath);
        Connector.setSave(savePath);
    }

    @FXML
    private void handleDefaultPath(ActionEvent event) {
        if (checkDefaultSaveDir.isSelected()) {
            savePathText.setText(srcPath);
            savePathText.setDisable(true);
            buttonChooseSavePath.setDisable(true);
            savePath = srcPath;

        } else {
            savePathText.setText(StringUtils.EMPTY);
            savePathText.setDisable(false);
            buttonChooseSavePath.setDisable(false);
            savePath = StringUtils.EMPTY;

        }
        Connector.setSave(savePath);
    }

    @FXML
    public void handleNextButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/files.fxml"));

        Stage currentStage = (Stage) savePathText.getScene().getWindow();
        currentStage.close();

        Stage stage = new Stage();
        stage.setTitle("Formating obfuscate config ");
        stage.setScene(new Scene(root, 500, 500));
        stage.show();
    }

    @Override
    public URL getFxmlUrl() {
        return getClass().getResource("/fxml/files.fxml");
    }
}
