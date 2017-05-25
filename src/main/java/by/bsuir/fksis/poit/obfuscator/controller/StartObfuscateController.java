package by.bsuir.fksis.poit.obfuscator.controller;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Иван on 09.05.2017.
 */
public class StartObfuscateController {
    @FXML
    public Button obfuscateButton = new Button();

    public void handleObfuscateButton(ActionEvent actionEvent) throws IOException {


    }




    private static void saveResult(CompilationUnit compilationUnit, String dest) throws IOException {
        String pack = compilationUnit.getPackageDeclaration().get().getNameAsString();
        pack = pack.replaceAll("\\.", "\\");
        dest += pack;
        String pathDir = dest;

        String newFileName = definePublicClassName(compilationUnit);
        if (!newFileName.isEmpty()) {
            newFileName = "\\" + newFileName + ".java";
            dest = pathDir + newFileName;
        }

        Path path = Paths.get(dest);

        Files.createDirectories(Paths.get(pathDir));

        File file = new File(dest);
        file.createNewFile();


        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(compilationUnit.toString().getBytes());
        fileOutputStream.close();
    }

    private static String definePublicClassName(CompilationUnit compilationUnit) {
        List<ClassOrInterfaceDeclaration> declarations = compilationUnit.getNodesByType(ClassOrInterfaceDeclaration.class);
        return declarations.stream()
                .filter(NodeWithModifiers::isPublic)
                .findFirst()
                .map(NodeWithSimpleName::getNameAsString)
                .orElse(StringUtils.EMPTY);
    }

}
