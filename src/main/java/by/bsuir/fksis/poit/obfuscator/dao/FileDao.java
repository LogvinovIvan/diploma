package by.bsuir.fksis.poit.obfuscator.dao;

import by.bsuir.fksis.poit.obfuscator.state.StateClass;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Иван on 16.05.2017.
 */
public class FileDao {

    public List<File> getSrcFiles(String path) {
        List<File> srcFiles = new ArrayList();
        try {
            srcFiles.addAll(Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList()));
        } catch (IOException ignored) {

        }
        return srcFiles;
    }


    public void saveFileInRequiredPackage(StateClass stateClass, String newPath) throws IOException {
        CompilationUnit compilationUnit = stateClass.getCompilationUnit();
        String pack = compilationUnit.getPackageDeclaration().get().getNameAsString();
        pack = pack.replace(".", "\\");
        newPath += "\\" + pack;
        String pathDir = newPath;

        String newFileName = definePublicClassName(compilationUnit);
        if (!newFileName.isEmpty()) {
            newFileName = "\\" + newFileName + ".java";
            newPath = pathDir + newFileName;
            Path path = Paths.get(newPath);

            Files.createDirectories(Paths.get(pathDir));

            File file = new File(newPath);
            file.createNewFile();


            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(stateClass.getContent().getBytes());
            fileOutputStream.close();
        }

    }

    private static String definePublicClassName(CompilationUnit compilationUnit) {
        List<ClassOrInterfaceDeclaration> declarations = compilationUnit.getNodesByType(ClassOrInterfaceDeclaration.class);
        String newName = declarations.stream()
                .filter(NodeWithModifiers::isPublic)
                .findFirst()
                .map(NodeWithSimpleName::getNameAsString)
                .orElse(StringUtils.EMPTY);
        if (newName.isEmpty()) {
            List<EnumDeclaration> declarations1 = compilationUnit.getNodesByType(EnumDeclaration.class);
            newName = declarations1.stream()
                    .filter(NodeWithModifiers::isPublic)
                    .findFirst()
                    .map(NodeWithSimpleName::getNameAsString)
                    .orElse(StringUtils.EMPTY);
        }

        return newName;
    }


}
