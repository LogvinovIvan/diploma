package by.bsuir.fksis.poit.obfuscator.controller;

/**
 * Created by Иван on 21.05.2017.
 */
public class Plugin {
    public Plugin(String name) {
        this.name = name;
    }

    public Plugin() {

    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
