package by.bsuir.fksis.poit.obfuscator.controller;

import by.bsuir.fksis.poit.obfuscator.plugin.loader.PluginManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Иван on 22.05.2017.
 */
public class PluginController implements Initializable {

    private PluginManager pluginManager = new PluginManager();

    @FXML
    private Button addPlugiButton = new Button();
    @FXML
    private TableView<Plugin> tableView = new TableView<>();

    @FXML
    public void handlePluginButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Jar files (*.jar)", "*.jar");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(new Stage());
        String path = file.getAbsolutePath();
        String result = pluginManager.addPlugin(path);
        if (result != null) {
            tableView.getItems().add(new Plugin(result));
            tableView.refresh();
        }

    }

    public void handleDelete(MouseEvent mouseEvent) throws FileNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + "Lexical plugin" + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Plugin selectedItem = tableView.getSelectionModel().getSelectedItem();
            tableView.getItems().remove(selectedItem);
            pluginManager.deletePlugin();
        }
    }

    public void handleBackButton(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/start.fxml"));

        Stage currentStage = (Stage) tableView.getScene().getWindow();
        currentStage.close();

        Stage stage = new Stage();
        stage.setTitle("Formating obfuscate config ");
        stage.setScene(new Scene(root, 340, 340));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Plugin> pl = pluginManager.getPlugins();
        pl.forEach(pl1 -> tableView.getItems().add(pl1));
        tableView.refresh();
    }
}
