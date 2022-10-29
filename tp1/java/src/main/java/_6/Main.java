package _6;

import java.io.*;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class Main {


    private static String certBase = "certificates-and-keys";


    // No verification of parameter numbers, if below will print exception
    
    public static void main(String[] args) throws IOException {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        boolean notEndStatus = true;
        String command = "";
        String [] commandBreakDown;
        System.out.println("Welcome to the program, write your command(write '-help' for the list of commands)");
        while(notEndStatus){
            command = input.readLine();
            commandBreakDown = command.split(" ");
            try {
                switch (commandBreakDown[0]) {
                    case ("-enc") -> encodePrepare(commandBreakDown[1], commandBreakDown[2]);
                    case ("-dec") -> decodePrepare(commandBreakDown[1], commandBreakDown[2], commandBreakDown[3]);
                    case ("-help") -> helpCommand();
                    case ("-end") -> notEndStatus = false;
                    default -> System.out.println("Command not found, please use command '-help' to know the commands that exist");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void encodePrepare(String filename, String certName) throws Exception {

        if(!Cert_Verification.verify("./" + certBase + "/end-entities/" + certName)){
            throw new Exception("Invalid certificate exception");
        }
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        FileInputStream fis = new FileInputStream("./" + certBase + "/end-entities/" + certName);
        Certificate cert = certificateFactory.generateCertificate(fis);

        String [] outFileNames = Encoder.encode(filename, cert);
        System.out.println("The filename for the cyphered file is '" + outFileNames[0] + "' and the " +
                "filename for the cyphered key is '" + outFileNames[1] + "'");
    }

    private static void decodePrepare(String encFileName, String encKeyFileName, String privateKeyFileName) throws Exception {

        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream("./" + certBase + "/pfx/" + privateKeyFileName), "changeit".toCharArray());
        Dec.decode(encFileName, encKeyFileName, ks);
        System.out.println("The file 'deciphered_" + encFileName + "' has the decoded information");
    }


    private static void helpCommand(){
        System.out.println("'-enc' with the following parameters, in this order, 'filename' with the " +
                        "filename of the file to encrypt and 'certificate' with the certificate, .cer " +
                        "file, to use to encrypt");
        System.out.println("'-dec' with the following parameters, in this order, 'cypheredFileName' " +
                "with the fileName to decipher, 'cypheredKeyFileName' with the File containing the" +
                "simetric key to use and 'privateKeyFileName' with the .pfx file to use");
        System.out.println("'-end' will end the application");
        System.out.println("'-help' to access the list of commands available");
    }
}