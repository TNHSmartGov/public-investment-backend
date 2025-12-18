package com.tnh.baseware.core.enums;

import com.tnh.baseware.core.enums.base.BaseEnum;
import com.tnh.baseware.core.exceptions.BWCGenericRuntimeException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum AlertSeverity implements BaseEnum<String> {
    INFO("info"),
    WARNING("warning"),
    CRITICAL("critical"),
    ERROR("error");

    String value;


    public static AlertSeverity fromValue(String value) {
        for (var severity : AlertSeverity.values()) {
            if (severity.getValue().equals(value)) {
                return severity;
            }
        }
        throw new BWCGenericRuntimeException("Unknown alert severity: " + value);
    }
}
