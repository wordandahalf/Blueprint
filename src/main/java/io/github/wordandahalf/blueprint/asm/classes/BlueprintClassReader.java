package io.github.wordandahalf.blueprint.asm.classes;

import org.objectweb.asm.ClassReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads a class from disk. See {@link ClassReader}
 */
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
                // TODO: Buffer overflow, either a) see if there is a limit on class size or b) just check the filesize
                byte[] data = new byte[4096];

                int bytesRead;
                while((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                    outputStream.write(data, 0, bytesRead);
                }

                outputStream.flush();
                byte[] output = outputStream.toByteArray();
                return output;
            } finally {
                if (close) {
                    inputStream.close();
                }

            }
        }
    }
}
