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
public enum SpringProfile implements BaseEnum<String> {
    DEVELOPMENT("dev"),
    PRODUCTION("prod");

    String value;

    public static SpringProfile fromValue(String value) {
        for (SpringProfile profile : values()) {
            if (profile.getValue().equalsIgnoreCase(value)) {
                return profile;
            }
        }
        throw new BWCGenericRuntimeException("Unknown value: " + value);
    }
}
