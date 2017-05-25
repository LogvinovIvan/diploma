package by.bsuir.fksis.poit.obfuscator.visitor.lexical;

import by.bsuir.fksis.poit.obfuscator.util.lexical.LexicalVisitorPriority;

/**
 * Created by Иван on 21.05.2017.
 */
public interface Prioritable {
    LexicalVisitorPriority getPriority();
}
