package by.bsuir.fksis.poit.obfuscator.controller;

import by.bsuir.fksis.poit.obfuscator.util.AbstractObfuscator;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class ProcessObfusactionController {


    private ObfuscationTask obfuscationTask;
    @FXML
    private ProgressIndicator progressIndicator = new ProgressIndicator(0);
    @FXML
    private Button startButton = new Button();
    @FXML
    private Button cancelButton = new Button();
    @FXML
    private ProgressBar progressBar = new ProgressBar(0);
    @FXML
    private Label statusLabel = new Label();


    @FXML
    public void handelStartButton(ActionEvent event) throws IOException {
        startButton.setDisable(true);
        progressBar.setProgress(0);
        progressIndicator.setProgress(0);
        cancelButton.setDisable(false);

        ObfuscationTask obfuscationTask = new ObfuscationTask();

        progressBar.progressProperty().unbind();

        progressBar.progressProperty().bind(obfuscationTask.progressProperty());

        progressIndicator.progressProperty().unbind();

        progressIndicator.progressProperty().bind(obfuscationTask.progressProperty());

        statusLabel.textProperty().unbind();

        statusLabel.textProperty().bind(obfuscationTask.messageProperty());

        // When completed tasks
        obfuscationTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                t -> {
                    List<File> copied = obfuscationTask.getValue();
                    statusLabel.textProperty().unbind();
                    statusLabel.setText("Copied: " + copied.size());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Finished");
                    alert.setHeaderText("Process was finished");
                    alert.setContentText("Total files was changed -" + copied.size() + ". Errors - 0");

                    alert.showAndWait();
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("/fxml/start.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Stage currentStage = (Stage) statusLabel.getScene().getWindow();
                    currentStage.close();

                    Stage stage = new Stage();
                    stage.setTitle("Formating obfuscate config ");
                    stage.setScene(new Scene(root, 500, 500));
                    stage.show();
                });

        // Start the Task.
        new Thread(obfuscationTask).start();


    }

    private AbstractObfuscator createChain() {
        AbstractObfuscator result = Connector.getObfuscators().pollFirst();
        AbstractObfuscator prev = result;
        for (AbstractObfuscator abstractObfuscator : Connector.getObfuscators()) {
            prev.setNext(abstractObfuscator);
            prev = abstractObfuscator;
        }

        return result;
    }


    public void handelCancelButton(ActionEvent event) {
        startButton.setDisable(false);
        cancelButton.setDisable(true);
        obfuscationTask.cancel(true);
        progressBar.progressProperty().unbind();
        progressIndicator.progressProperty().unbind();
        statusLabel.textProperty().unbind();
        //
        progressBar.setProgress(0);
        progressIndicator.setProgress(0);
    }
}
