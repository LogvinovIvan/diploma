package by.bsuir.fksis.poit.obfuscator.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Иван on 21.05.2017.
 */
public class WindowManager {
    private Map<Class, URL> bindingOfFilterWindows = new HashMap<>();
    private Class initControllerClass = SyntaxPageController.class;
    private URL initUrl = getClass().getResource("/fxml/ProgressObfuscationWindow.fxml");

    private static WindowManager instance;

    public static WindowManager getInstance() {
        return instance;
    }

    public static void initWindowManager(LinkedList<Class> classes, LinkedList<URL> urls) {
        instance = new WindowManager(classes, urls);
    }


    private WindowManager(LinkedList<Class> classes, LinkedList<URL> urls) {
        classes.addFirst(initControllerClass);
        urls.addLast(initUrl);

        for (int i = 0; i < classes.size(); i++) {
            bindingOfFilterWindows.put(classes.get(i), urls.get(i));
        }
    }

    public URL getUrlWindow(Class controllerClass) {
        return bindingOfFilterWindows.get(controllerClass);
    }


}
