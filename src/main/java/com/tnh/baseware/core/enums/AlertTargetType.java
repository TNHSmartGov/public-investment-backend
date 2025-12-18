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
public enum AlertTargetType implements BaseEnum<String> {

    USER_SUBSCRIPTION("USER_SUBSCRIPTION", "User Subscription", "Đăng ký của người dùng"),
    PAYMENT("PAYMENT", "Payment", "Thanh toán"),
    USERS("USERS", "Users", "Người dùng");

    String value;
    String name;
    String displayName;

    public static AlertTargetType fromValue(String value) {
        for (var alertTargetType : AlertTargetType.values()) {
            if (alertTargetType.getValue().equals(value)) {
                return alertTargetType;
            }
        }
        throw new BWCGenericRuntimeException("Unknown AlertTargetType value: " + value);
    }
}
