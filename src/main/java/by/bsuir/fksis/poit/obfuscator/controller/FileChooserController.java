package by.bsuir.fksis.poit.obfuscator.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.util.ResourceBundle;

/**
 * Created by Иван on 09.05.2017.
 */
public class FileChooserController implements FilterController, Initializable {

    private String srcPath;
    private String libPath;
    private String savePath;

    private boolean srcChecked;
    private boolean saveChecked;

    public FileChooserController() throws IOException {

    }

    @FXML
    private Button buttonChoseSrcFile = new Button();

    @FXML
    private Button buttonChooseLibFile = new Button();

    @FXML
    private Button buttonChooseSavePath = new Button();

    @FXML
    private Button nextButton = new Button();

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
        srcChecked = true;
        updateButton();
    }


    @FXML
    private void handleButtonLibPath(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(new Stage());
        libPath = file.getAbsolutePath();
        libPathText.setText(libPath);
        Connector.setLib(libPath);
        updateButton();
    }

    @FXML
    private void handleButtonSavePath(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(new Stage());
        savePath = file.getAbsolutePath();
        savePathText.setText(savePath);
        Connector.setSave(savePath);
        saveChecked = true;
        updateButton();
    }

    @FXML
    private void handleDefaultPath(ActionEvent event) {
        if(srcChecked){
            if (checkDefaultSaveDir.isSelected()) {
                savePathText.setText(srcPath);
                savePathText.setDisable(true);
                buttonChooseSavePath.setDisable(true);
                savePath = srcPath;
                saveChecked = true;
            } else {
                savePathText.setText(StringUtils.EMPTY);
                savePathText.setDisable(false);
                buttonChooseSavePath.setDisable(false);
                savePath = StringUtils.EMPTY;
                saveChecked = false;
            }
            Connector.setSave(savePath);

        }
        updateButton();
    }

    @FXML
    public void handleNextButton(ActionEvent event) throws IOException {
        if(srcChecked && saveChecked){
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/files.fxml"));

            Stage currentStage = (Stage) savePathText.getScene().getWindow();
            currentStage.close();

            Stage stage = new Stage();
            stage.setTitle("Formating obfuscate config ");
            stage.setScene(new Scene(root, 500, 500));
            stage.show();
        }
    }

    @Override
    public URL getFxmlUrl() {
        return getClass().getResource("/fxml/files.fxml");
    }

    private void updateButton(){
        if(saveChecked && srcChecked){
            nextButton.setDisable(false);
        }else {
            nextButton.setDisable(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nextButton.setDisable(true);
    }
}
