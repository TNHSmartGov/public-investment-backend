package com.tnh.baseware.core.services;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import static org.junit.jupiter.api.Assertions.fail;

public class MissingMessagesKeysTest {

    private static final Pattern GET_MESSAGE_PATTERN =
            Pattern.compile("getMessage\\(\"([^\"]+)\"\\)");

    //@Test
    void detectMissingKeys() throws IOException {
        // Load messages.properties
        Properties props = new Properties();
        props.load(Files.newBufferedReader(Paths.get("src/main/resources/i18n/messages.properties")));

        // Scan all Java files
        List<String> missingKeys = new ArrayList<>();
        Path srcPath = Paths.get("src/main/java");
        Files.walk(srcPath)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(file -> {
                    try {
                        String content = Files.readString(file);
                        Matcher matcher = GET_MESSAGE_PATTERN.matcher(content);
                        while (matcher.find()) {
                            String key = matcher.group(1);
                            if (!props.containsKey(key)) {
                                missingKeys.add(key + " (in " + file.getFileName() + ")");
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        if (!missingKeys.isEmpty()) {
            fail("Missing keys in messages.properties:\n" + String.join("\n", missingKeys));
        }
    }
}