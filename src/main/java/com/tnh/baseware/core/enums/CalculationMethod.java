package com.tnh.baseware.core.enums;

import com.tnh.baseware.core.enums.base.BaseEnum;
import com.tnh.baseware.core.exceptions.BWCGenericRuntimeException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum CalculationMethod implements BaseEnum<String> {
    FIXED("FIXED", "fixed", "Tiền cố định"),
    PERCENTAGE("PERCENTAGE", "percentage", "Phần trăm");

    String value;
    String name;
    String displayName;

    public static CalculationMethod fromValue(String value) {
        for (CalculationMethod type : CalculationMethod.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new BWCGenericRuntimeException("Unknown value: " + value);
    }
}
