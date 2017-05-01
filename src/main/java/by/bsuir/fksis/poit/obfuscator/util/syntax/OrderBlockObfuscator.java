package by.bsuir.fksis.poit.obfuscator.util.syntax;

import by.bsuir.fksis.poit.obfuscator.state.StateClass;
import by.bsuir.fksis.poit.obfuscator.util.AbstractObfuscator;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.util.Collections;

/**
 * Created by Иван on 08.04.2017.
 */
public class OrderBlockObfuscator extends AbstractObfuscator {


    public void obfuscate() {

    }

    @Override
    public void obfuscate(StateClass stateClass) {

        NodeList<TypeDeclaration<?>> types = stateClass.getCompilationUnit().getTypes();
        NodeList<TypeDeclaration<?>> newOrderTypes = new NodeList<>();
        while (types.iterator().hasNext()) {
            TypeDeclaration<?> type = types.iterator().next();
            Collections.shuffle(type.getMembers());
            type.remove();
            newOrderTypes.add(type);
        }

        newOrderTypes.forEach(t -> stateClass.getCompilationUnit().addType(t));

        System.out.print(stateClass.getCompilationUnit());
    }
}
