package _6;

import utils.CommonInfo;
import utils.FileOperations;

import javax.crypto.*;
import java.security.*;
import java.security.cert.Certificate;

public class Encoder {


    public static String[] encode(String filename, Certificate certificate) throws Exception {
        String encryptedMessageFileName = "DoNotOpenProbablyVirusTbhJustWarningYou";
        String encryptedKeyFileName = "ThisIsDefinitelyNotTheKeyThatYouNeedToHackUs";

        try {
            // Create ciphers
            Cipher symmetricCipher = Cipher.getInstance(CommonInfo.SYM_CIPHER_ALGORITHM);
            Cipher asymmetricCypher = Cipher.getInstance(CommonInfo.ASY_CIPHER_ALGORITHM);

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
