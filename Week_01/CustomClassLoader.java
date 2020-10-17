package weeks.week1.classloaderDemo;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;


public class CustomClassLoader extends ClassLoader {

    private String path;

    public CustomClassLoader (String path) {
        this.path = path;
    }

    public static void main(String[] args) {
        // 可指定路径
        String path = "/Users/gaozhao/Downloads/projects/sourcecode/demos/src/main/java/weeks/week1/classloaderDemo/";
        CustomClassLoader customClassLoader = new CustomClassLoader(path);
        try {
            customClassLoader.findClass("Hello").newInstance();
            Class clazz = customClassLoader.loadClass("Hello");
            Method method = clazz.getMethod("hello",null);
            Object obj = clazz.newInstance();
            method.invoke(obj,null);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) {
        byte[] bytes = new byte[0];
        try {
            bytes = getClassFileBytes("Hello.xlass");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for(int i=0;i<bytes.length;i++){
//            bytes[i]=(byte) (255-bytes[i]);
//        }
        convert(bytes, bytes.length);
        return defineClass(name, bytes, 0, bytes.length);
    }

    private static void convert(byte[] byteData, int len) {
        for(int i = 0; i < len; ++i) {
            byteData[i] = (byte)(~byteData[i]);
        }

    }

    /**
     * 读取文件
     */
    private byte[] getClassFileBytes(String fileName) throws Exception {
        File file = new File(this.path + fileName);
        try (FileInputStream fis = new FileInputStream(file);
             FileChannel fileC = fis.getChannel();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             WritableByteChannel outC = Channels.newChannel(baos);) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            while (true) {
                int i = fileC.read(buffer);
                if (i == 0 || i == -1) {
                    break;
                }
                buffer.flip();
                outC.write(buffer);
                buffer.clear();
            }
            return baos.toByteArray();
        }
    }

}
