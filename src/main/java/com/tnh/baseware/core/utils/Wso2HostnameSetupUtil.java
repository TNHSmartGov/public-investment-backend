package com.tnh.baseware.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Wso2HostnameSetupUtil {

    public static void generateKeystoreWithCN(String keystorePath, String alias, String password, String hostname) throws IOException, InterruptedException {
        // CN=Common Name, OU=Organizational Unit, O=Organization, L=Locality, S=State, C=Country
        var dname = "CN=" + hostname + ", OU=Is, O=Ivms, L=TNH, S=TNH, C=VN";
        var pb = new ProcessBuilder(
                "keytool",
                "-genkey",
                "-alias", alias,
                "-keyalg", "RSA",
                "-keysize", "2048",
                "-keystore", keystorePath,
                "-storetype", "JKS",
                "-dname", dname,
                "-storepass", password,
                "-keypass", password,
                "-validity", "3650"
        );

        pb.inheritIO();
        var process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            log.info(LogStyleHelper.info("Keystore created at: {}"), keystorePath);
        } else {
            log.error(LogStyleHelper.error("Failed to create keystore. Exit code: {}"), exitCode);
        }
    }

    public static void exportPublicCertJKS(String keystorePath, String alias, String password, String outputPath)
            throws IOException, InterruptedException {

        var pb = new ProcessBuilder(
                "keytool",
                "-export",
                "-alias", alias,
                "-file", outputPath,
                "-keystore", keystorePath,
                "-storetype", "JKS",
                "-storepass", password
        );

        pb.inheritIO();
        var process = pb.start();
        var exitCode = process.waitFor();

        if (exitCode == 0) {
            log.info(LogStyleHelper.info("Exported public certificate to: {}"), outputPath);
        } else {
            log.error(LogStyleHelper.error("Failed to export public cert from JKS. Exit code: {}"), exitCode);
        }
    }

    public static void importCertToTruststoreJKS(String truststorePath, String alias, String certFilePath, String password)
            throws IOException, InterruptedException {

        var pb = new ProcessBuilder(
                "keytool",
                "-import",
                "-alias", alias,
                "-file", certFilePath,
                "-keystore", truststorePath,
                "-storetype", "JKS",
                "-storepass", password,
                "-noprompt"
        );

        pb.inheritIO();
        var process = pb.start();
        var exitCode = process.waitFor();

        if (exitCode == 0) {
            log.info(LogStyleHelper.info("Imported certificate to truststore (JKS): {}"), truststorePath);
        } else {
            log.error(LogStyleHelper.error("Failed to import certificate to JKS. Exit code: {}"), exitCode);
        }
    }

    public static void generateKeystorePKCS12WithCN(String keystorePath, String alias, String password, String
            hostname) throws IOException, InterruptedException {
        // CN=Common Name, OU=Organizational Unit, O=Organization, L=Locality, S=State, C=Country
        var dname = "CN=" + hostname + ", OU=Is, O=Ivms, L=TNH, S=TNH, C=VN";
        var pb = new ProcessBuilder(
                "keytool",
                "-genkey",
                "-alias", alias,
                "-keyalg", "RSA",
                "-keysize", "2048",
                "-keystore", keystorePath,
                "-storetype", "PKCS12",
                "-dname", dname,
                "-storepass", password,
                "-keypass", password,
                "-validity", "3650"
        );

        pb.inheritIO();
        var process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            log.info(LogStyleHelper.info("Keystore created at: {}"), keystorePath);
        } else {
            log.error(LogStyleHelper.error("Failed to create keystore. Exit code: {}"), exitCode);
        }
    }

    public static void exportPublicCertPKCS12(String keystorePath, String alias, String password, String outputPath)
            throws IOException, InterruptedException {

        var pb = new ProcessBuilder(
                "keytool",
                "-export",
                "-alias", alias,
                "-file", outputPath,
                "-keystore", keystorePath,
                "-storetype", "PKCS12",
                "-storepass", password
        );

        pb.inheritIO();
        var process = pb.start();
        var exitCode = process.waitFor();

        if (exitCode == 0) {
            log.info(LogStyleHelper.info("Exported public certificate to: {}"), outputPath);
        } else {
            log.error(LogStyleHelper.error("Failed to export public cert from PKCS12. Exit code: {}"), exitCode);
        }
    }

    public static void importCertToTruststorePKCS12(String truststorePath, String alias, String certFilePath, String password)
            throws IOException, InterruptedException {

        var pb = new ProcessBuilder(
                "keytool",
                "-import",
                "-alias", alias,
                "-file", certFilePath,
                "-keystore", truststorePath,
                "-storetype", "PKCS12",
                "-storepass", password,
                "-noprompt"
        );

        pb.inheritIO();
        var process = pb.start();
        var exitCode = process.waitFor();

        if (exitCode == 0) {
            log.info(LogStyleHelper.info("Imported certificate to truststore (PKCS12): {}"), truststorePath);
        } else {
            log.error(LogStyleHelper.error("Failed to import certificate to PKCS12. Exit code: {}"), exitCode);
        }
    }


    public static void main(String[] args) throws Exception {
        var hostname = "is.dev.ivms.com";
        var keystoreJKSPath = "src/main/resources/tls/ivms.jks";
        var keystorePKCS12Path = "src/main/resources/tls/ivms.p12";
        var alias = "ivms";
        var password = "ivms12345678";

        generateKeystoreWithCN(keystoreJKSPath, alias, password, hostname);
        exportPublicCertJKS(keystoreJKSPath, alias, password, "src/main/resources/tls/ivms.pem");
        importCertToTruststoreJKS(keystoreJKSPath, alias, "src/main/resources/tls/ivms.pem", password);

        generateKeystorePKCS12WithCN(keystorePKCS12Path, alias, password, hostname);
        exportPublicCertPKCS12(keystorePKCS12Path, alias, password, "src/main/resources/tls/ivms.crt");
        importCertToTruststorePKCS12("client-truststore.p12", alias, "src/main/resources/tls/ivms.pem", password);
    }
}
