package utils;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import java.io.*;

public class FileOperations {

    public static byte[] readFile(String filename) throws IOException {
        byte[] data = null;
        File file = new File(filename);
        checkFile(file, filename);
        FileInputStream fis = new FileInputStream(filename);
        data = fis.readAllBytes();
        fis.close();
        return data;
    }

    public static void writeToFile(byte[] data, String outFilename) throws IOException {
        FileOutputStream fos = new FileOutputStream(outFilename);
        fos.write(data);
        fos.close();
    }

    public static byte[] readFile64(String filename) throws IOException {
        File file = new File(filename);
        checkFile(file, filename);
        byte[] data = null;
        FileInputStream fis = new FileInputStream(filename);
        Base64InputStream input = new Base64InputStream(fis);
        data = input.readAllBytes();
        input.close();
        return data;
    }

    public static void writeToFile64(byte[] data, String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        Base64OutputStream output = new Base64OutputStream(fos);
        output.write(data);
        output.close();
    }

    private static void checkFile(File file, String filename) throws FileNotFoundException {
        if(!file.exists() && !file.canRead()){
            throw new FileNotFoundException("file: '" + filename + "' not found or cannot read");
        }
    }
}
