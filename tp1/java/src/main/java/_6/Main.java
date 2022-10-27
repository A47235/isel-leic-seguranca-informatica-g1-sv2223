package _6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        boolean notEndStatus = true;
        String command = "";
        String [] commandBreakDown;
        System.out.println("Welcome to the program, write your command(write '-help' for the list of commands)");
        while(notEndStatus){
            command = input.readLine();
            commandBreakDown = command.split(" ");
            switch (commandBreakDown[0]) {
                case ("-enc") -> encodePrepare(commandBreakDown[1], commandBreakDown[2]);
                case ("-dec") -> decodePrepare(commandBreakDown[1], commandBreakDown[2], commandBreakDown[3]);
                case ("-help") -> helpCommand();
                case ("-end") -> notEndStatus = false;
                default -> System.out.println("Command not found, please use command '-help' to know the commands that exist");
            }
        }
    }

    private static void encodePrepare(String filename, String certName){

    }

    private static void decodePrepare(String encFileName, String encKeyFileName, String keystoreFileName){

    }


    private static void helpCommand(){
        System.out.println("'-enc' with the following parameters, in this order, 'filename' with the " +
                        "filename of the file to encrypt and 'certificate' with the certificate, .cer " +
                        "file, to use to encrypt");
        System.out.println("'-dec' with the following parameters, in this order, 'cypheredFileName' " +
                "with the fileName to decipher, 'cypheredKeyFileName' with the File containing the" +
                "simetric key to use and 'keystoreFileName' with the keystore to use");
        System.out.println("'-end' will end the application");
        System.out.println("'-help' to access the list of commands available");
    }
}