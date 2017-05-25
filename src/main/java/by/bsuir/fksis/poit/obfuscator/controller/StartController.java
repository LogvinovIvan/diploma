package by.bsuir.fksis.poit.obfuscator.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Иван on 21.05.2017.
 */
public class StartController {
    @FXML
    private Button obfusccate = new Button();

    @FXML
    private Button plugin = new Button();

    @FXML
    private Button about = new Button();


    @FXML
    public void handleObfuscate(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/FileChoserWindow.fxml"));

        Stage currentStage = (Stage) obfusccate.getScene().getWindow();
        currentStage.close();

        Stage stage = new Stage();
        stage.setTitle("Initial data chooser");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @FXML
    public void handlePlugin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/plugins.fxml"));

        Stage currentStage = (Stage) obfusccate.getScene().getWindow();
        currentStage.close();

        Stage stage = new Stage();
        stage.setTitle("Plugin editor");
        stage.setScene(new Scene(root, 320, 340));
        stage.show();
    }


    @FXML
    public void handleAbout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/about.fxml"));

        Stage currentStage = (Stage) obfusccate.getScene().getWindow();
        currentStage.close();

        Stage stage = new Stage();
        stage.setTitle("Plugin editor");
        stage.setScene(new Scene(root, 500, 500));
        stage.show();

    }


}
