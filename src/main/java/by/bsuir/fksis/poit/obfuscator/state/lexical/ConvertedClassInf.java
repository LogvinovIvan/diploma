package by.bsuir.fksis.poit.obfuscator.state.lexical;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Иван on 24.04.2017.
 */
public class ConvertedClassInf {


    private static ConverterOfMappingMethod converterOfMappingMethod = new ConverterOfMappingMethod();

    private Map<String, String> mapClass = new HashMap<>();
    private Map<String, String> mapFullClassName = new HashMap<>();
    private Map<String, String> mapFullMethod = new HashMap<>();
    private Map<String, String> mapFieldsMethod = new HashMap<>();

    private Map<String, LexicalClassNameInf> lexicalClassNameInfMap = new HashMap<>();

    public LexicalClassNameInf getLexicalClassNameInf(String nameClass) {
        return lexicalClassNameInfMap.get(nameClass);
    }

    public Map<String, String> getMapSimpleNameMethod() {
        return mapSimpleNameMethod;
    }

    public void setMapSimpleNameMethod(Map<String, String> mapSimpleNameMethod) {
        this.mapSimpleNameMethod = mapSimpleNameMethod;
    }

    private Map<String, String> mapSimpleNameMethod = new HashMap<>();
    private HashMap<String, Boolean> markerIsStaticmethod = new HashMap<>();


    public ConvertedClassInf(List<LexicalClassNameInf> lexicalClassNameInfList) {
        for (LexicalClassNameInf classNameInf : lexicalClassNameInfList) {
            mapClass.put(classNameInf.getOldFullName(), classNameInf.getNewClassName());
            mapFullClassName.put(classNameInf.getOldFullName(), classNameInf.getNewFullName());

            for (Map.Entry<String, String> entryMethod : classNameInf.getMethods().entrySet()) {
                mapFullMethod.put(classNameInf.getOldClassName() + "." + entryMethod.getKey(), entryMethod.getValue());
                mapSimpleNameMethod.put(entryMethod.getKey(), entryMethod.getValue());

            }

            markerIsStaticmethod.putAll(classNameInf.getMethodsIsStatic());


            for (Map.Entry<String, String> entryField : classNameInf.getFields().entrySet()) {
                mapFieldsMethod.put(classNameInf.getOldClassName() + "." + entryField.getKey(), entryField.getValue());
            }
            lexicalClassNameInfMap.put(classNameInf.getFullName(), classNameInf);


        }
        converterOfMappingMethod.convert(mapSimpleNameMethod);
    }

    public boolean containsClass(String oldClassName) {
        return mapClass.containsKey(oldClassName);
    }

    public boolean containsFullClassName(String oldFullClassName) {
        return mapFullClassName.containsKey(oldFullClassName);
    }

    public boolean containsPackage(String oldPackage) {
        return mapFullClassName.containsKey(oldPackage);
    }

    public String getNewClassName(String oldClassName) {
        return mapClass.get(oldClassName);
    }

    public String getNewFullClassName(String oldClassFullName) {
        return mapFullClassName.get(oldClassFullName);
    }

    public String getNewPackageName(String oldPackageName) {
        return mapFullClassName.get(oldPackageName);
    }

    public boolean containsMethod(String methodDeclaration) {
        return mapFullMethod.containsKey(methodDeclaration);
    }

    public String getNewNameMethod(String methodDeclaration) {
        return mapFullMethod.get(methodDeclaration);
    }

    public boolean containsField(String filed) {
        return mapFieldsMethod.containsKey(filed);
    }

    public String getNewNameField(String field) {
        return mapFieldsMethod.get(field);
    }


    public Map<String, String> getMapFullMethod() {
        return mapFullMethod;
    }

    public Map<String, String> getMapFieldsMethod() {
        return mapFieldsMethod;
    }

    private boolean isStaticMethod(String signature) {
        return markerIsStaticmethod.containsKey(signature);
    }

    public Map<String, Boolean> getStaticMethodMarkerMap() {
        return markerIsStaticmethod;
    }


}
