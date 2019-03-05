package io.github.wordandahalf.blueprint.asm;

import java.lang.reflect.Method;
import java.util.HashMap;

public class BlueprintClassLoader {
    private HashMap<String, Class> modifiedClasses = new HashMap<>();

    public static Class defineClass(String name, byte[] bytecode) throws Exception {
        Class clazz = ClassLoader.class;

        Method method = clazz.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        method.setAccessible(true);

        Object result = method.invoke(ClassLoader.getSystemClassLoader(), name, bytecode, 0, bytecode.length);

        if(result instanceof Class) {
            return (Class) result;
        }

        return null;
    }
}
