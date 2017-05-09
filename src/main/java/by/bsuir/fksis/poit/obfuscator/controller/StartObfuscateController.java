package by.bsuir.fksis.poit.obfuscator.controller;

import by.bsuir.fksis.poit.obfuscator.state.StateClass;
import by.bsuir.fksis.poit.obfuscator.util.AbstractObfuscator;
import com.github.javaparser.JavaParser;
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
import java.util.stream.Collectors;

/**
 * Created by Иван on 09.05.2017.
 */
public class StartObfuscateController {
    @FXML
    public Button obfuscateButton = new Button();

    public void handleObfuscateButton(ActionEvent actionEvent) throws IOException {
        AbstractObfuscator obfuscator = createChain();
        File file = new File(Connector.getSrc());
        List<File> filesInFolder = Files.walk(Paths.get(file.getAbsolutePath()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        for (File sicFile : filesInFolder) {
            CompilationUnit cu = JavaParser.parse(file);
            StateClass stateClass = new StateClass(cu);
            obfuscator.obfuscate(stateClass);
        }

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

    private static void saveResult(CompilationUnit compilationUnit, String dest) throws IOException {
        String pack = dest.replaceAll(".+?src", "");
        dest = dest.replaceAll("test project", "result");
        String pathDir = dest.replaceAll("\\\\(?:.(?!\\\\))+$", StringUtils.EMPTY);

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
