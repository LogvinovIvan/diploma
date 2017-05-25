package by.bsuir.fksis.poit.obfuscator.util;

import by.bsuir.fksis.poit.obfuscator.config.PriorityObfuscatorLevel;
import by.bsuir.fksis.poit.obfuscator.state.StateClass;

/**
 * Created by Иван on 21.05.2017.
 */
public interface IAbstractObfuscator {
    void setNext(AbstractObfuscator next);

    AbstractObfuscator getNext();

    void obfuscate(StateClass stateClass);

    PriorityObfuscatorLevel getLevel();
}
