package _6;

import utils.CommonInfo;
import utils.FileOperations;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

public class Dec {
/*
    filename dec.decode(FilenameCifer, FilenameKey, keystore);
*/
    public static String decode(String filenameCiphered, String filenameKey, KeyStore keystore) throws Exception {
        try{
            Cipher cifraSim = Cipher.getInstance(CommonInfo.SYM_CIPHER_ALGORITHM);
            Cipher cifraAssim = Cipher.getInstance(CommonInfo.ASY_CIPHER_ALGORITHM);
            Key privKey = keystore.getKey(keystore.aliases().nextElement(), "changeit".toCharArray());

            // Get symmetric key
            byte[] cipheredKey = FileOperations.readFile64(filenameKey);
            cifraAssim.init(Cipher.DECRYPT_MODE, privKey);
            byte[] decipheredKey = cifraAssim.doFinal(cipheredKey);

            // Decode file
            byte[] cipheredFile = FileOperations.readFile64(filenameCiphered);
            SecretKey secretKey = new SecretKeySpec(decipheredKey, "AES");
            cifraSim.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(CommonInfo.gcmLen, CommonInfo.iv));
            byte [] decipheredFile = cifraSim.doFinal(cipheredFile);

            // Write to output
            String outFile = "deciphered_" + filenameCiphered;

            FileOperations.writeToFile(decipheredFile,outFile);
            return outFile;
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            // should not happen
            throw new Exception("The cipher does not exist");
        }
    }
}
