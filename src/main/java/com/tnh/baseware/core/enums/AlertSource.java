package com.tnh.baseware.core.enums;

import com.tnh.baseware.core.enums.base.BaseEnum;
import com.tnh.baseware.core.exceptions.BWCGenericRuntimeException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum AlertSource implements BaseEnum<String> {
    SYSTEM("system"),
    USER("user"),
    DEVICE("device");

    String value;

    public static AlertSource fromValue(String value) {
        for (var source : AlertSource.values()) {
            if (source.getValue().equals(value)) {
                return source;
            }
        }
        throw new BWCGenericRuntimeException("Unknown alert source: " + value);
    }
}
