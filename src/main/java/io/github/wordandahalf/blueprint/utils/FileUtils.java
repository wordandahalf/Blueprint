package io.github.wordandahalf.blueprint.utils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtils {
    public static List<ClassNode> loadFolder(File folder, boolean recursion) throws IOException {
        List<ClassNode> loadedNodes = new ArrayList<>();

        if(folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    loadedNodes.add(loadClassFile(file));
                }

                if (file.getName().endsWith(".jar")) {
                    loadedNodes.addAll(loadJar(file));
                }

                if (file.isDirectory() && recursion) {
                    loadedNodes.addAll(loadFolder(folder, recursion));
                }
            }
        }

        if (folder.getName().endsWith(".class")) {
            loadedNodes.add(loadClassFile(folder));
        }

        if (folder.getName().endsWith(".jar")) {
            loadedNodes.addAll(loadJar(folder));
        }

        return loadedNodes;
    }

    public static ClassNode loadClassFile(File file) throws IOException  {
        if(file.getName().endsWith(".class")) {
            FileInputStream is = new FileInputStream(file);
            ClassReader reader = new ClassReader(is);
            is.close();

            ClassNode node = new ClassNode();
            reader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            return node;
        }

        return null;
    }

    public static List<ClassNode> loadJar(File file) {
        try {
            JarFile jar = new JarFile(file);
            List<ClassNode> list = new ArrayList<>();
            Enumeration<JarEntry> enumeration = jar.entries();
            while(enumeration.hasMoreElements()) {
                JarEntry next = enumeration.nextElement();
                if(next.getName().endsWith(".class")) {
                    ClassReader reader = new ClassReader(jar.getInputStream(next));
                    ClassNode node = new ClassNode();
                    reader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                    list.add(node);
                }
            }
            jar.close();
            return list;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
