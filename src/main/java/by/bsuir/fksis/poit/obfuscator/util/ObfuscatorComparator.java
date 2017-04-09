package by.bsuir.fksis.poit.obfuscator.util;

import java.util.Comparator;

/**
 * Created by Иван on 09.04.2017.
 */
public class ObfuscatorComparator implements Comparator<AbstractObfuscator> {
    @Override
    public int compare(AbstractObfuscator o1, AbstractObfuscator o2) {
        if (o1.getLevel().getLevel() > o2.getLevel().getLevel()) {
            return 1;
        } else if (o1.getLevel().getLevel() < o2.getLevel().getLevel()) {
            return -1;
        } else {
            return 0;
        }
    }
}
