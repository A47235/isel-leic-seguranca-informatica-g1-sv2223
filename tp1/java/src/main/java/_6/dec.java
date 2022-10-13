package _6;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

public class dec {
/*
    filename dec.decode(FilenameCifer, FilenameKey, keystore);
*/
    public String decode(String FilenameCifered, String FilenameKey, KeyStore keystore) throws Exception {
        Cipher cifra;
        try{
            // TODO add clear cipher
            cifra = Cipher.getInstance("");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            // should not happen
            throw new Exception("The cipher does not exist");
        }
        Key privKey = keystore.getKey(keystore.aliases().nextElement(), "changeit".toCharArray());
        cifra.init(Cipher.DECRYPT_MODE, privKey);

        // TODO
        byte [] fileBytes = new byte[1];
        //TODO change to get file in Base64


        cifra.update(fileBytes);

        /*TODO: change*/
        return null;
    }

}
