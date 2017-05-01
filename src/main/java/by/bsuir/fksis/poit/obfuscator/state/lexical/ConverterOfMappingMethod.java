package by.bsuir.fksis.poit.obfuscator.state.lexical;

import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Иван on 01.05.2017.
 */
public class ConverterOfMappingMethod {

    private final static String MARKER_OVERRIDE = "override:";

    public void convert(Map<String, String> mapMethod) {

        Set<String> executeSignatures = new HashSet<>();
        List<String> noneMapedMethods = mapMethod.entrySet().stream()
                .filter(entry -> entry.getValue().contains(MARKER_OVERRIDE))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Iterator<String> methodIterator = noneMapedMethods.iterator();
        while (methodIterator.hasNext()) {
            String methodSignature = methodIterator.next();
            if (!executeSignatures.contains(methodSignature)) {
                Deque<String> chainOfMethods = defineChain(mapMethod, methodSignature);
                fillRequiredSignatures(mapMethod, chainOfMethods);
                executeSignatures.addAll(chainOfMethods);
            } else {
                methodIterator.remove();
            }
        }
    }

    private Deque<String> defineChain(Map<String, String> mapField, String overridedMethod) {
        String newNameMethod = mapField.get(overridedMethod);
        Deque<String> chainOfMethods = new LinkedList<>();
        do {
            chainOfMethods.add(overridedMethod);
            overridedMethod = newNameMethod.replaceFirst(MARKER_OVERRIDE, StringUtils.EMPTY);
            newNameMethod = mapField.get(overridedMethod);
        } while (newNameMethod != null && newNameMethod.contains(MARKER_OVERRIDE));
        chainOfMethods.add(overridedMethod);
        return chainOfMethods;
    }

    private void fillRequiredSignatures(Map<String, String> mapMethods, Deque<String> chain) {
        String head = chain.getLast();
        String newMethodName;
        if (mapMethods.keySet().contains(head)) {
            newMethodName = mapMethods.get(head);
        } else {
            String[] partsMethod = head.split("\\.");
            newMethodName = partsMethod[partsMethod.length - 1];
            newMethodName = newMethodName.replaceAll("\\(.+", StringUtils.EMPTY);
        }

        String finalNewMethodName = newMethodName;
        chain.forEach(methodSignature -> mapMethods.put(methodSignature, finalNewMethodName));
    }
}
