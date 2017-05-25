package by.bsuir.fksis.poit.obfuscator.plugin.loader;

import by.bsuir.fksis.poit.obfuscator.controller.Plugin;
import by.bsuir.fksis.poit.obfuscator.controller.WindowManager;
import org.xeustechnologies.jcl.JarClassLoader;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

/**
 * Created by Иван on 16.05.2017.
 */
public class PluginManager {

    private static final String PATH_TO_JAR_LIBS = "modules";
    private static final String CONFIG_FILE_PATH = "conf/config-modules.txt";
    private static final String CONF_FILE_DELIMETER = "::";

    private static final Integer JAR_NAME_INDEX = 0;
    private static final Integer CONTROLLER_NAME_INDEX = 1;
    private static final Integer FXML_FILE_INDEX = 2;

    private static final String MAIN_CLASS_ATRIBUTE = "Main-class";
    private static final String FXML_FILE_ATTRIBUTE = "fxml-file";


    private LinkedList<URL> urlList = new LinkedList<>();
    private LinkedList<Class> classes = new LinkedList<>();

    public PluginManager() {

        List<MetaInfJarData> dataList = readMetaInfjar();
        JarClassLoader jcl = new JarClassLoader();
        dataList.forEach(metaInfJarData -> {


            try {

                try {
                    Class clazz = getClass().getClassLoader().loadClass(metaInfJarData.controllerName);
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


                URL url = new File(PATH_TO_JAR_LIBS + "/fxml/" + metaInfJarData.fxmlFileName).toURI().toURL();
                urlList.add(url);

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }


    public String addPlugin(String jarFilePath) {
        JarClassLoader jcl = new JarClassLoader();
        jcl.add(jarFilePath);

        JarInputStream jarStream = null;
        try {
            jarStream = new JarInputStream(new FileInputStream(jarFilePath));
            Manifest mf = jarStream.getManifest();
            String fxml = mf.getMainAttributes().getValue("fxml-file");
            String mainClass = mf.getMainAttributes().getValue("Main-class");
            String name = mf.getMainAttributes().getValue("name-module");
            addInfToConfFile(name, mainClass, fxml);

            String path = getClass().getClassLoader().getResource("").toURI().getPath() + mainClass.replace(".", "\\") + ".class";
            File file1 = new File(path);
            file1.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            fileOutputStream.write(jcl.getLoadedResources().get(mainClass.replace(".", "/") + ".class"));
            fileOutputStream.close();

            path = PATH_TO_JAR_LIBS + "\\fxml\\" + fxml;
            file1 = new File(path);
            file1.createNewFile();
            fileOutputStream = new FileOutputStream(file1);
            fileOutputStream.write(jcl.getLoadedResources().get("fxml/" + fxml));
            fileOutputStream.close();


            return name;

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void applyNewPlugin(String classname, String pathFxml) throws ClassNotFoundException, MalformedURLException {

        Class cl = getClass().getClassLoader().loadClass(classname);
        File file = new File(pathFxml);
        URL fxmlURL = file.toURI().toURL();

        urlList.add(fxmlURL);
        classes.add(cl);
        WindowManager.initWindowManager(classes, urlList);
    }

    private void addInfToConfFile(String name, String mainClass, String fxmlFile) throws IOException {
        FileWriter fw = new FileWriter(CONFIG_FILE_PATH, true); //the true will append the new data
        fw.write(name + "::" + mainClass + "::" + fxmlFile + "\n");
        fw.close();
    }

    public LinkedList<URL> getUrlList() {
        return urlList;
    }

    public LinkedList<Class> getClasses() {
        return classes;
    }

    public List<MetaInfJarData> readMetaInfjar() {
        List<MetaInfJarData> metaInfJarDataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CONFIG_FILE_PATH)))) {
            while (reader.ready()) {
                String jarInf = reader.readLine();
                String[] partInf = jarInf.split(CONF_FILE_DELIMETER);
                MetaInfJarData data = new MetaInfJarData();

                data.setControllerName(partInf[CONTROLLER_NAME_INDEX]);
                data.setName(partInf[JAR_NAME_INDEX]);
                data.setFxmlFileName(partInf[FXML_FILE_INDEX]);

                metaInfJarDataList.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metaInfJarDataList;
    }


    public void deletePlugin() throws FileNotFoundException {
        URL url = urlList.pollLast();
        urlList.pollLast();
        urlList.add(url);

        classes.pollLast();

        PrintWriter writer = new PrintWriter(new File(CONFIG_FILE_PATH));
        writer.print("");
        writer.close();
    }


    static class MetaInfJarData {

        public MetaInfJarData() {

        }

        public MetaInfJarData(String name, String controllerName, String fxmlFileName) {
            this.name = name;
            this.controllerName = controllerName;
            this.fxmlFileName = fxmlFileName;
        }

        private String name;
        private String controllerName;
        private String fxmlFileName;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getControllerName() {
            return controllerName;
        }

        public void setControllerName(String controllerName) {
            this.controllerName = controllerName;
        }

        public String getFxmlFileName() {
            return fxmlFileName;
        }

        public void setFxmlFileName(String fxmlFileName) {
            this.fxmlFileName = fxmlFileName;
        }
    }


    public List<Plugin> getPlugins() {
        return readMetaInfjar().stream().map(metaInfJarData -> new Plugin(metaInfJarData.name)).collect(Collectors.toList());
    }

}
