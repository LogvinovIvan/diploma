package by.bsuir.fksis.poit.obfuscator.util.lexical;

/**
 * Created by Иван on 21.05.2017.
 */
public enum LexicalVisitorPriority {
    LOW(0), MID(1), HIGH(2), SUPERHIGH(3);

    int priority;

    LexicalVisitorPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
