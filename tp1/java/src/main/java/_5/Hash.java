package _5;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Hash {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        if (args.length == 2) {
            String algorithm = args[0];
            String file = args[1];

            MessageDigest md = MessageDigest.getInstance(algorithm);

            byte[] msg = getFileBytes(file);
            md.update(msg);
            byte[] hash = md.digest();

            hashPrint(hash);
        } else System.out.println("bad args");

    }


    private static void hashPrint(byte[] h) {
        for (byte b : h) {
            System.out.printf("%02x", b);
        }
        System.out.println();
    }




    private static byte[] getFileBytes(String name) throws IOException {

        File file = new File(name);

        if (file.exists()) {
            return Files.readAllBytes(file.toPath());
        }

        throw new FileNotFoundException(name + " does not exist.");

    }




}