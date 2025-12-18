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
public enum TokenType implements BaseEnum<String> {
    ACCESS("access"),
    REFRESH("refresh");

    String value;

    public static TokenType fromValue(String value) {
        for (var type : TokenType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new BWCGenericRuntimeException("Unknown token type: " + value);
    }
}
