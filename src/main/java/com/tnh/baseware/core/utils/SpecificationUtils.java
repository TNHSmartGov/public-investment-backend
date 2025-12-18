package com.tnh.baseware.core.utils;

import java.util.UUID;

public class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static Object convertValue(Class<?> fieldType, Object value) {
        if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
            return Integer.parseInt(value.toString());
        } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
            return Long.parseLong(value.toString());
        } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
            return Double.parseDouble(value.toString());
        } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
            return Boolean.parseBoolean(value.toString());
        } else if (fieldType.equals(UUID.class)) {
            return UUID.fromString(value.toString());
        }
        return value;
    }
}
