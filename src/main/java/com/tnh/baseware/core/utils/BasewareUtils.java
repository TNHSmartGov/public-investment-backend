package com.tnh.baseware.core.utils;

import jakarta.persistence.Entity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import ua_parser.Parser;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class BasewareUtils {

    private static final Parser UA_PARSER = new Parser();

    public static boolean isBlank(Object value) {
        switch (value) {
            case null -> {
                return true;
            }
            case String s -> {
                return s.isBlank();
            }
            case Collection<?> c -> {
                return c.isEmpty();
            }
            case Map<?, ?> m -> {
                return m.isEmpty();
            }
            case Optional<?> o -> {
                return o.isEmpty();
            }
            case Object[] o -> {
                return o.length == 0;
            }
            default -> {
                return isArrayEmpty(value);
            }
        }
    }

    public static <T> List<T> findDuplicates(List<T> list) {
        Map<T, Long> counts = list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return counts.entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();
    }

    private static boolean isArrayEmpty(Object value) {
        if (value.getClass().isArray()) {
            return Array.getLength(value) == 0;
        }
        return false;
    }

    public static String getBaseUrl(HttpServletRequest request) {
        var scheme = request.getScheme();
        var serverName = request.getServerName();
        var serverPort = request.getServerPort();

        var isDefaultPort = (scheme.equals("http") && serverPort == 80)
                || (scheme.equals("https") && serverPort == 443);

        return scheme + "://" + serverName + (isDefaultPort ? "" : ":" + serverPort);
    }

    public static Set<Class<?>> getEntities(String pathEntity) {
        var reflections = new Reflections(pathEntity);
        return reflections.getTypesAnnotatedWith(Entity.class);
    }

    public static List<String> getMethods(String clazzName) {
        Class<?> clazz;
        try {
            clazz = Class.forName(clazzName);
        } catch (Exception e) {
            log.error(LogStyleHelper.error("Class not found: {}"), e.getMessage());
            return new ArrayList<>();
        }
        var methods = new ArrayList<String>();
        var declaredMethods = clazz.getDeclaredMethods();
        addMethods(clazz, methods, declaredMethods);

        var superclass = clazz.getSuperclass();
        if (superclass == null) {
            return methods;
        }
        declaredMethods = superclass.getDeclaredMethods();
        addMethods(superclass, methods, declaredMethods);

        return methods;
    }

    public static String pluralize(String entityName) {
        if (entityName == null || entityName.isEmpty()) {
            return entityName;
        }
        var snakeCaseName = toSnakeCase(entityName);
        if (snakeCaseName.endsWith("s") || snakeCaseName.endsWith("x") || snakeCaseName.endsWith("z")
                || snakeCaseName.endsWith("ch") || snakeCaseName.endsWith("sh")) {
            return snakeCaseName + "es";
        } else if (snakeCaseName.endsWith("y") && !isVowel(snakeCaseName.charAt(snakeCaseName.length() - 2))) {
            return snakeCaseName.substring(0, snakeCaseName.length() - 1) + "ies";
        } else {
            return snakeCaseName + "s";
        }
    }

    public static String toSnakeCase(String str) {
        var result = new StringBuilder();
        result.append(Character.toLowerCase(str.charAt(0)));
        for (var i = 1; i < str.length(); i++) {
            var c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append('_').append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static boolean isVowel(char c) {
        return "aeiou".indexOf(Character.toLowerCase(c)) != -1;
    }

    private static void addMethods(Class<?> clazz, List<String> methods, Method[] declaredMethods) {
        for (var method : declaredMethods) {
            if (method.getDeclaringClass().equals(clazz) && !method.isSynthetic()) {
                methods.add(method.getName());
            }
        }
    }

    public static String getPlatform(String userAgent) {
        try {
            var client = UA_PARSER.parse(userAgent);
            return client.os.family; // ex: "iOS", "Android", "Windows", "Mac", "Linux"
        } catch (Exception e) {
            log.error(LogStyleHelper.error("Error parsing user agent: {}"), e.getMessage());
            return "Unknown";
        }
    }

    public static String getBrowser(String userAgent) {
        try {
            var client = UA_PARSER.parse(userAgent);
            return client.userAgent.family; // ex: "Chrome", "Safari", "Firefox"
        } catch (Exception e) {
            log.error(LogStyleHelper.error("Error parsing user agent: {}"), e.getMessage());
            return "Unknown";
        }
    }

    public static String getDevice(String userAgent) {
        try {
            var client = UA_PARSER.parse(userAgent);
            return client.device.family; // ex: "iPhone", "iPad", "Samsung", "Xiaomi"
        } catch (Exception e) {
            log.error(LogStyleHelper.error("Error parsing user agent: {}"), e.getMessage());
            return "Unknown";
        }
    }

    public static String generateDeviceId(HttpServletRequest request) {
        var ipAddress = request.getRemoteAddr();
        var userAgent = request.getHeader("User-Agent");
        var acceptLang = request.getHeader("Accept-Language");

        var badUserAgents = Set.of("Other", "Unknown", "Generic", "", "null", "undefined", "N/A", "No User Agent");

        if (userAgent == null || badUserAgents.contains(userAgent.trim())) {
            userAgent = UUID.randomUUID().toString();
        }

        if (acceptLang == null) {
            acceptLang = "";
        }

        String raw = ipAddress + "|" + userAgent + "|" + acceptLang;
        return sha512Hex(raw);
    }

    private static String sha512Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error(LogStyleHelper.error("SHA-512 not available: {}"), e.getMessage());
            return UUID.randomUUID().toString();
        }
    }

    private static String bytesToHex(byte[] bytes) {
        var sb = new StringBuilder();
        for (var b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        var a = BasewareUtils.toSnakeCase("LogTrackActivityAPI");
        log.info("a: {}", a);
    }
}
