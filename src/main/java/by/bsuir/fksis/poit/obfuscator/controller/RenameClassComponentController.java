package by.bsuir.fksis.poit.obfuscator.controller;

import by.bsuir.fksis.poit.obfuscator.util.lexical.RenameClassComponentsObfuscator;
import by.bsuir.fksis.poit.obfuscator.util.lexical.factory.LexicalFactory;
import by.bsuir.fksis.poit.obfuscator.util.lexical.factory.RenameClassFactory;
import by.bsuir.fksis.poit.obfuscator.util.lexical.factory.RenameFieldFactory;
import by.bsuir.fksis.poit.obfuscator.util.lexical.factory.RenameMethodFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.io.IOException;
import java.util.TreeSet;

/**
 * Created by Иван on 09.05.2017.
 */
public class RenameClassComponentController {

    private String src = Connector.getSrc();
    private String lib = Connector.getLib();

    private TreeSet<LexicalFactory> lexicalFactories = new TreeSet<>();


    @FXML
    private CheckBox renameClassCheckBox;
    @FXML
    private CheckBox renameMethodCheckBox;
    @FXML
    private CheckBox renameFieldCheckBox;
    @FXML
    private Button nextPage = new Button();


    @FXML
    public void handleRanameClassCheckBox(ActionEvent event) {
        if (renameClassCheckBox.isSelected()) {
            lexicalFactories.add(new RenameClassFactory());
        } else {
            lexicalFactories.removeIf(lexicalFactory -> lexicalFactory instanceof RenameClassFactory);
        }
    }

    @FXML
    public void handleRenameMethodCheckBox(ActionEvent event) {
        if (renameMethodCheckBox.isSelected()) {
            lexicalFactories.add(new RenameMethodFactory());
        } else {
            lexicalFactories.removeIf(lexicalFactory -> lexicalFactory instanceof RenameMethodFactory);
        }
    }

    @FXML
    public void handleRenameFieldCheckBox(ActionEvent event) {
        if (renameFieldCheckBox.isSelected()) {
            lexicalFactories.add(new RenameFieldFactory());
        } else {
            lexicalFactories.removeIf(lexicalFactory -> lexicalFactory instanceof RenameFieldFactory);
        }
    }


    @FXML
    public void handleNextButton(ActionEvent event) throws IOException {
        Connector.addObfuscator(new RenameClassComponentsObfuscator(src, lib, lexicalFactories));
    }

}
