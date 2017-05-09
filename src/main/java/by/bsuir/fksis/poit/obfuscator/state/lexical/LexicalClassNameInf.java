package by.bsuir.fksis.poit.obfuscator.state.lexical;

import java.util.HashMap;

/**
 * Created by Иван on 15.04.2017.
 */
public class LexicalClassNameInf {
    private String oldClassName;
    private String newClassName;
    private String packageName;
    private String fullName;

    private HashMap<String, String> methods = new HashMap<>();
    private HashMap<String, Boolean> methodsIsStatic = new HashMap<>();
    private HashMap<String, String> fields = new HashMap<>();


    public LexicalClassNameInf() {
    }

    public String getOldClassName() {
        return oldClassName;
    }

    public void setOldClassName(String oldClassName) {
        this.oldClassName = oldClassName;
    }

    public String getNewClassName() {
        return newClassName;
    }

    public void setNewClassName(String newClassName) {
        this.newClassName = newClassName;
    }

    public HashMap<String, String> getMethods() {
        return methods;
    }

    public void addMethods(String oldName, String newName) {
        methods.put(oldName, newName);
    }

    public HashMap<String, String> getFields() {
        return fields;
    }

    public void addField(String newField, String oldField) {
        fields.put(oldField, newField);
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOldFullName() {
        return packageName + "." + oldClassName;
    }

    public String getNewFullName() {
        return packageName + "." + newClassName;
    }

    public void setNewMethodSignature(String oldSignature, String newSignature) {
        methods.put(oldSignature, newSignature);
    }

    public void setStaticMarkerForMethod(String methodName, Boolean isStatic) {
        methodsIsStatic.put(methodName, isStatic);
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public HashMap<String, Boolean> getMethodsIsStatic() {
        return methodsIsStatic;
    }
}
