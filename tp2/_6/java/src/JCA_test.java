import javax.net.ssl.*;
import java.io.IOException;

// codigo retirado de https://github.com/isel-deetc-computersecurity/seginf-inv2223-public/blob/main/JCA/SSLClient.java

public class JCA_test {
    public static void main(String[] args) throws IOException {
        SSLSocketFactory sslFactory;

        // print cipher suites avaliable at the client
        String[] cipherSuites = sslFactory.getSupportedCipherSuites();
        for (int i=0; i<cipherSuites.length; ++i) {
            System.out.println("option " + i + " " + cipherSuites[i]);
        }

        // establish connection
        SSLSocket client = (SSLSocket) sslFactory.createSocket("www.secure-server.edu", 4433);
        client.startHandshake();
        SSLSession session = client.getSession();
        System.out.println("Cipher suite: " + session.getCipherSuite());
        System.out.println("Protocol version: " + session.getProtocol());
        System.out.println(session.getPeerCertificates()[0]);
    }
}
// ir à pasta do jre instalado, ir à pasta bin e, utilizando o comando 'keytool -import -file "<CA1.pem-path>"'

