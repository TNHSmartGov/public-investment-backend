package com.tnh.baseware.core.specs;

import com.tnh.baseware.core.utils.LogStyleHelper;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public enum FieldType {

    BOOLEAN {
        public Object parse(String value) {
            return Boolean.valueOf(value);
        }
    },

    CHAR {
        public Object parse(String value) {
            return value.charAt(0);
        }
    },

    DATE {
        public Object parse(String value) {
            Object date = null;
            try {
                var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                date = LocalDateTime.parse(value, formatter);
            } catch (Exception e) {
                log.error(LogStyleHelper.error("Failed parse field type DATE {}"), e.getMessage());
            }

            return date;
        }
    },

    DOUBLE {
        public Object parse(String value) {
            return Double.valueOf(value);
        }
    },

    INTEGER {
        public Object parse(String value) {
            return Integer.valueOf(value);
        }
    },

    LONG {
        public Object parse(String value) {
            return Long.valueOf(value);
        }
    },

    UUID {
        public Object parse(String value) {
            try {
                return java.util.UUID.fromString(value);
            } catch (IllegalArgumentException e) {
                log.error(LogStyleHelper.error("Failed parse field type UUID {}"), e.getMessage());
                return null;
            }
        }
    },

    STRING {
        public Object parse(String value) {
            return value;
        }
    };

    public abstract Object parse(String value);
}
