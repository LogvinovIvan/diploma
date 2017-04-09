package by.bsuir.fksis.poit.obfuscator.util.syntax;

import by.bsuir.fksis.poit.obfuscator.config.PriorityObfuscatorLevel;
import by.bsuir.fksis.poit.obfuscator.state.StateClass;
import by.bsuir.fksis.poit.obfuscator.util.AbstractObfuscator;

/**
 * Created by Иван on 08.04.2017.
 */
public class SpaceObfuscator extends AbstractObfuscator {

    {
        level = PriorityObfuscatorLevel.HIGH;
    }

    public SpaceObfuscator() {

    }

    @Override
    public void obfuscate(StateClass stateClass) {
        String content = stateClass.getContent().replaceAll("[\\s]{2,}", " ");
        content = content.replaceAll("\\n", " ");
        stateClass.setContent(content);
    }
}
