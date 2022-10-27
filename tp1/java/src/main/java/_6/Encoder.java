package main.java._6;

import main.java.utils.FileOperations;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.util.Base64;

public class Encoder {
    String SYM_CIPHER_ALGORITHM = "AES/GCM/NOPADDING";
    String ASY_CIPHER_ALGORITHM = "RSA";

    public Encoder() {}

    public Encoder(String symCipherAlgorithm, String asyCipherAlgorithm) {
        SYM_CIPHER_ALGORITHM = symCipherAlgorithm;
        ASY_CIPHER_ALGORITHM = asyCipherAlgorithm;
    }

    public String[] encode(String filename, Certificate certificate) throws Exception {
        String encryptedMessageFileName = "DoNotOpenProbablyVirusTbhJustWarningYou";
        String encryptedKeyFileName = "ThisIsDefinitelyNotTheKeyThatYouNeedToHackUs";

        try {
            // Create ciphers
            Cipher symmetricCipher = Cipher.getInstance(SYM_CIPHER_ALGORITHM);
            Cipher asymmetricCypher = Cipher.getInstance(ASY_CIPHER_ALGORITHM);

            // Generate symmetric key
            SecretKey secretKey = KeyGenerator.getInstance("AES/GCM/NOPADDING").generateKey();

            // Encode file
            symmetricCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedMessage= symmetricCipher.doFinal(FileOperations.readFile(filename));
            FileOperations.writeToFile64(encryptedMessage, encryptedMessageFileName);

            // Encode symmetric key
            asymmetricCypher.init(Cipher.ENCRYPT_MODE, certificate);
            byte[] encryptedKey = asymmetricCypher.doFinal(secretKey.getEncoded());
            FileOperations.writeToFile64(encryptedMessage, encryptedKeyFileName);

        } catch (NoSuchPaddingException | IllegalBlockSizeException |NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            // should not happen
            throw new Exception("The cipher does not exist");
        }

        return new String[] {encryptedMessageFileName, encryptedKeyFileName};
    }
}
