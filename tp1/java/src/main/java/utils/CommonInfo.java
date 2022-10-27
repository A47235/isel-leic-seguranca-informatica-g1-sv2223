package utils;

public class CommonInfo {

    public static String SYM_CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    public static String ASY_CIPHER_ALGORITHM = "RSA";

    public static int gcmLen = 128;
    public static byte[] iv = java.util.Base64.getEncoder().encode("my_iv_string".getBytes());
}
