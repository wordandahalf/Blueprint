package io.github.wordandahalf.blueprint.loading;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlueprintClassLoader {
    private ClassLoader parent;
    private List<URL> urls;

    public BlueprintClassLoader(ClassLoader parent) {
        this.parent = parent;
        this.urls = new ArrayList<>();
    }

    public BlueprintClassLoader(ClassLoader parent, URL... urls) {
        this.parent = parent;
        this.urls = new ArrayList<>();

        this.urls.addAll(Arrays.asList(urls));
    }

    public BlueprintClassLoader(ClassLoader parent, String... paths) throws MalformedURLException {
        this.parent = parent;
        this.urls = new ArrayList<>();

        for(String path : paths) {
           this.addPath(path);
        }
    }

    public void addPath(String path) throws MalformedURLException {
        this.urls.add(new File(path).toURI().toURL());
    }

    public URLClassLoader toClassLoader() {
        if(urls.size() == 0)
            return new URLClassLoader(urls.toArray(new URL[0]), parent);
        return new URLClassLoader(urls.toArray(new URL[urls.size()]), parent);
    }
}
