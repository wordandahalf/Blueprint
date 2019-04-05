package io.github.wordandahalf.blueprint.classes;

import org.objectweb.asm.ClassReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BlueprintClassReader extends ClassReader {
    private BlueprintClassReader(byte[] bytecode) {
        super(bytecode);
    }

    public BlueprintClassReader(String className, ClassLoader loader) throws IOException {
        this(readStream(loader.getResourceAsStream(className.replace('.', '/') + ".class"), true));
    }

    private static byte[] readStream(InputStream inputStream, boolean close) throws IOException {
        if (inputStream == null) {
            throw new IOException("Class not found");
        } else {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] data = new byte[4096];

                int bytesRead;
                while((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                    outputStream.write(data, 0, bytesRead);
                }

                outputStream.flush();
                byte[] var5 = outputStream.toByteArray();
                return var5;
            } finally {
                if (close) {
                    inputStream.close();
                }

            }
        }
    }
}
