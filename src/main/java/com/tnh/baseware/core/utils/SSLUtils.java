package com.tnh.baseware.core.utils;

import com.tnh.baseware.core.exceptions.BWCGenericRuntimeException;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

@Slf4j
public class SSLUtils {

    private SSLUtils() {
    }

    public static void configureSslTrustForSelfSignedCert(String certPath) {
        try (var certStream = loadCertInputStream(certPath)) {

            var cf = CertificateFactory.getInstance("X.509");
            var cert = cf.generateCertificate(certStream);

            var trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            trustStore.setCertificateEntry("wso2carbon", cert);

            var tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            log.debug(LogStyleHelper.debug("Custom SSL trust for WSO2 applied successfully."));
        } catch (Exception e) {
            log.error(LogStyleHelper.error("Failed to apply custom SSL trust: {}"), e.getMessage());
            throw new BWCGenericRuntimeException("Failed to apply custom SSL trust", e);
        }
    }

    private static InputStream loadCertInputStream(String certPath) throws IOException {
        if (certPath.startsWith("classpath:")) {
            var resourcePath = certPath.substring("classpath:".length());
            var is = SSLUtils.class.getClassLoader().getResourceAsStream(resourcePath);
            if (is == null) {
                throw new FileNotFoundException("Not found: " + resourcePath);
            }
            return is;
        } else {
            return new FileInputStream(certPath);
        }
    }
}
