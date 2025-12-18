package com.tnh.baseware.core.utils;

import com.tnh.baseware.core.exceptions.BWCGenericRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;

@Slf4j
public class CertificateUtils {

    private static final String DEFAULT_PASSWORD = "wso2carbon";

    private static File getFileFromClasspath() {
        try {
            return new ClassPathResource("tls/wso2carbon.p12").getFile();
        } catch (Exception e) {
            log.error(LogStyleHelper.error("Failed to load file from classpath: {}"), e.getMessage());
            return null;
        }
    }

    public static void exportKeyAndCertFromKeystore(File keystoreFile, String password,
            File certOutFile, File keyOutFile) throws Exception {

        var keystore = KeyStore.getInstance("PKCS12");
        try (var fis = java.nio.file.Files.newInputStream(keystoreFile.toPath())) {
            keystore.load(fis, password.toCharArray());
        }

        var aliases = keystore.aliases();
        if (!aliases.hasMoreElements()) {
            log.debug(LogStyleHelper.debug("Keystore has no aliases."));
            throw new BWCGenericRuntimeException("Keystore has no aliases.");
        }

        var alias = aliases.nextElement();
        var key = keystore.getKey(alias, password.toCharArray());
        if (!(key instanceof PrivateKey privateKey)) {
            log.debug(LogStyleHelper.debug("Key is not a private key."));
            throw new BWCGenericRuntimeException("Not a private key");
        }

        var cert = (X509Certificate) keystore.getCertificate(alias);

        try (var writer = new OutputStreamWriter(new FileOutputStream(certOutFile), StandardCharsets.UTF_8)) {
            writer.write("-----BEGIN CERTIFICATE-----\n");
            writer.write(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(cert.getEncoded()));
            writer.write("\n-----END CERTIFICATE-----\n");
        }

        try (var writer = new OutputStreamWriter(new FileOutputStream(keyOutFile), StandardCharsets.UTF_8)) {
            writer.write("-----BEGIN PRIVATE KEY-----\n");
            writer.write(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(privateKey.getEncoded()));
            writer.write("\n-----END PRIVATE KEY-----\n");
        }

        log.debug(LogStyleHelper.debug("Exported cert to: {}"), certOutFile.getPath());
        log.debug(LogStyleHelper.debug("Exported key to: {}"), keyOutFile.getPath());
    }

    public static void exportCrtFromKeystore(File keystoreFile, String password, File crtOutFile) throws Exception {
        var keystore = KeyStore.getInstance("PKCS12");
        try (var fis = java.nio.file.Files.newInputStream(keystoreFile.toPath())) {
            keystore.load(fis, password.toCharArray());
        }

        var aliases = keystore.aliases();
        if (!aliases.hasMoreElements()) {
            throw new BWCGenericRuntimeException("No aliases found in keystore");
        }

        var alias = aliases.nextElement();
        var cert = (X509Certificate) keystore.getCertificate(alias);

        try (var fos = new FileOutputStream(crtOutFile)) {
            fos.write(cert.getEncoded());
        }

        log.debug(LogStyleHelper.debug("Exported cert to (DER): {}"), crtOutFile.getPath());
    }

    public static void main(String[] args) {
        try {
            var p12File = getFileFromClasspath();

            var tlsDir = new File("src/main/resources/tls");
            var certOut = new File(tlsDir, "wso2carbon-cert.pem");
            var keyOut = new File(tlsDir, "wso2carbon-key.pem");
            var crtOut = new File(tlsDir, "wso2carbon.crt");

            assert p12File != null;
            exportKeyAndCertFromKeystore(p12File, DEFAULT_PASSWORD, certOut, keyOut);
            exportCrtFromKeystore(p12File, DEFAULT_PASSWORD, crtOut);

            log.info(LogStyleHelper.info("Done exporting cert and key."));
        } catch (Exception e) {
            log.error(LogStyleHelper.error("Error: {}"), e.getMessage());
        }
    }
}