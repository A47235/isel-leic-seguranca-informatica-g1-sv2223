package _6;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.List;

public class cert_Verification {

    static final String intCertsLocation = "certificates-and-keys/cert-int";
    static final String certsLocation = "certificates-and-keys/end-entities";
    static final String trustLocation = "certificates-and-keys/trust-anchors/CA.jks";

    public cert_Verification() throws CertificateException {
    }

    private static CertStore getCerts() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, CertificateException, IOException {

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        File folder = new File(intCertsLocation);
        File[] listOfFiles = folder.listFiles();

        List<Certificate> list = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                    Certificate cert = cf.generateCertificate(fis);
                    list.add(cert);
                }
            }
        }

        folder = new File(certsLocation);
        listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                    Certificate cert = cf.generateCertificate(fis);
                    list.add(cert);
                }
            }
        }

        CertStoreParameters collection = new CollectionCertStoreParameters(list);
        return CertStore.getInstance("Collection", collection);


    }


    private static KeyStore getKeyStore() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        final String pass = "changeit";

        try (java.io.FileInputStream fis = new java.io.FileInputStream(trustLocation)) {
            ks.load(fis, pass.toCharArray());
        }

        return ks;
    }

    private static CertSelector getCertSelector(String path) throws IOException, CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        final X509CertSelector cs = new X509CertSelector();
        try (java.io.FileInputStream fis = new java.io.FileInputStream(path)) {
            Certificate cert = cf.generateCertificate(fis);
            cs.setCertificate((X509Certificate) cert);

        }
        return cs;
    }

    static boolean verify(String certPath) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, CertPathBuilderException, CertStoreException {

        KeyStore ks = getKeyStore();

        CertSelector selector = getCertSelector(certPath);

        CertStore store = getCerts();

        PKIXBuilderParameters builder = new PKIXBuilderParameters(ks, selector);
        builder.addCertStore(store);
        builder.setRevocationEnabled(false);


        

        CertPathBuilder builder1 = CertPathBuilder.getInstance("PKIX");
        try {
            builder1.build(builder);
        } catch (CertPathBuilderException e) {
            throw e;
            //return false;
        }
        return true;

    }

    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException, CertificateException, KeyStoreException, NoSuchAlgorithmException, CertPathBuilderException, CertStoreException {
        System.out.println(verify("certificates-and-keys/end-entities/Alice_2.cer"));
    }

}
