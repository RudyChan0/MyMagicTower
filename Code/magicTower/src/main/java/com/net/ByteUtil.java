package com.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class ByteUtil {

    public static String getString(ByteBuffer byteBuffer) {
        Charset charset = Charset.forName("utf-8");
        CharBuffer charBuffer = charset.decode(byteBuffer);
        return charBuffer.toString();
    }

    public static Object getObject(ByteBuffer byteBuffer) throws ClassNotFoundException, IOException {
        InputStream input = new ByteArrayInputStream(byteBuffer.array());
        ObjectInputStream oin = new ObjectInputStream(input);
        Object object = oin.readObject();
        input.close();
        oin.close();
        byteBuffer.clear();
        return object;
    }

    public static ByteBuffer getByteBuffer(String string) {
        return ByteBuffer.wrap(string.getBytes());
    }

    public static ByteBuffer getByteBuffer(Serializable obj) throws IOException {
        return ByteBuffer.wrap(getBytes(obj));
    }

    public static byte[] getBytes(Serializable obj) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        out.flush();
        byte[] bytes = bout.toByteArray();
        bout.close();
        out.close();
        return bytes;
    }

    public static int sizeOf(Serializable obj) throws IOException {
        return getBytes(obj).length;
    }
}
