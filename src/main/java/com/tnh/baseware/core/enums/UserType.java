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
public enum UserType implements BaseEnum<String> {
    CUSTOMER("CUSTOMER", "customer", "Khách hàng"),
    AGENT("AGENT", "agent", "Nhân viên"),
    MANAGER("MANAGER", "manager", "Quản lý");

    String value;
    String name;
    String displayName;

    public static UserType fromValue(String value) {
        for (UserType type : UserType.values()) {
            if (type.getValue().toString().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new BWCGenericRuntimeException("Unknown value: " + value);
    }

}
