package by.bsuir.fksis.poit.obfuscator.config;

/**
 * Created by Иван on 09.04.2017.
 */
public enum PriorityObfuscatorLevel {
    LOW(1), MIDDLE(2), HIGH(3);

    private int level;

    PriorityObfuscatorLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
