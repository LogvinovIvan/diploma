package by.bsuir.fksis.poit.obfuscator.util.comparator;

import com.github.javaparser.symbolsolver.model.methods.MethodUsage;
import com.github.javaparser.symbolsolver.model.typesystem.Type;

import java.util.Comparator;

/**
 * Created by Иван on 25.04.2017.
 */
public class MethodUsageComparator implements Comparator<MethodUsage> {

    @Override
    public int compare(MethodUsage o1, MethodUsage o2) {
        String name1 = o1.getName();
        String name2 = o2.getName();
        if (o1.getName().equals(o2.getName())) {
            if (o1.getParamTypes().size() == o2.getParamTypes().size()) {
                for (int i = 0; i < o1.getParamTypes().size(); i++) {
                    Type paramType1 = o1.getParamType(i);
                    Type paramType2 = o2.getParamType(i);
                    if (!paramType1.isTypeVariable() && !paramType2.isTypeVariable() && !paramType1.equals(paramType2)) {
                        return -1;
                    }
                }
                return 0;
            } else {
                return -1;
            }

        } else {
            return -1;
        }

    }
}
