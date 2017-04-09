package by.bsuir.fksis.poit.obfuscator.util;

import by.bsuir.fksis.poit.obfuscator.config.PriorityObfuscatorLevel;
import by.bsuir.fksis.poit.obfuscator.state.StateClass;

/**
 * Created by Иван on 08.04.2017.
 */
public abstract class AbstractObfuscator {

    protected PriorityObfuscatorLevel level;
    protected AbstractObfuscator next;

    public AbstractObfuscator() {
    }


    public AbstractObfuscator(AbstractObfuscator next) {
        this.next = next;
    }

    public void setNext(AbstractObfuscator next) {
        this.next = next;
    }

    public AbstractObfuscator getNext() {
        return next;
    }


    public abstract void obfuscate(StateClass stateClass);

    public PriorityObfuscatorLevel getLevel() {
        return this.level;
    }

}
