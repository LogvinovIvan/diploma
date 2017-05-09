package by.bsuir.fksis.poit.obfuscator.controller;

import by.bsuir.fksis.poit.obfuscator.util.AbstractObfuscator;
import by.bsuir.fksis.poit.obfuscator.util.ObfuscatorComparator;

import java.util.TreeSet;

/**
 * Created by Иван on 09.05.2017.
 */
public class Connector {
    private static String src;
    private static String lib;
    private static String save;
    private static TreeSet<AbstractObfuscator> obfuscators = new TreeSet<>(new ObfuscatorComparator());

    public static String getSrc() {
        return src;
    }

    public static void setSrc(String src) {
        Connector.src = src;
    }

    public static String getLib() {
        return lib;
    }

    public static void setLib(String lib) {
        Connector.lib = lib;
    }

    public static String getSave() {
        return save;
    }

    public static void setSave(String save) {
        Connector.save = save;
    }

    public static TreeSet<AbstractObfuscator> getObfuscators() {
        return obfuscators;
    }

    public static void addObfuscator(AbstractObfuscator obfuscator) {
        Connector.obfuscators = obfuscators;
    }
}
