package com.tnh.baseware.core.utils;

import java.util.Objects;
import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?\\d{10,15}$");
    private static final Pattern IDN_PATTERN = Pattern.compile("^\\d{9,12}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^\\w{3,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,}$");

    private ValidationUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(Objects.requireNonNullElse(email, "")).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return PHONE_PATTERN.matcher(Objects.requireNonNullElse(phoneNumber, "")).matches();
    }

    public static boolean isValidIdentificationNumber(String id) {
        return IDN_PATTERN.matcher(Objects.requireNonNullElse(id, "")).matches();
    }

    public static boolean isValidUsername(String username) {
        return USERNAME_PATTERN.matcher(Objects.requireNonNullElse(username, "")).matches();
    }

    public static boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(Objects.requireNonNullElse(password, "")).matches();
    }
}
