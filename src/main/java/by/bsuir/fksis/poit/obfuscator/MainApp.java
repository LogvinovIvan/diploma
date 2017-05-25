package by.bsuir.fksis.poit.obfuscator;/**
 * Created by Иван on 09.04.2017.
 */

import by.bsuir.fksis.poit.obfuscator.controller.WindowManager;
import by.bsuir.fksis.poit.obfuscator.plugin.loader.PluginManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        PluginManager pluginManager = new PluginManager();
        WindowManager.initWindowManager(pluginManager.getClasses(), pluginManager.getUrlList());

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/start.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 340, 340));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
