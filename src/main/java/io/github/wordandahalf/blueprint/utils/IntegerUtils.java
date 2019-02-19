package io.github.wordandahalf.blueprint.utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class IntegerUtils {
    public static int fromBytes(byte... bytes) {
        return new BigInteger(bytes).intValue();
    }

    public static byte[] toBytes(int integer) {
        return ByteBuffer.allocate(4).putInt(integer).array();
    }
}
