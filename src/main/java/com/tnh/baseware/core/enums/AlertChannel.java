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
public enum AlertChannel implements BaseEnum<String> {

    EMAIL("EMAIL", "Email", "Email"),
    NOTIFICATION("NOTIFICATION", "Notification", "Thông báo"),
    SYSTEM_LOG("SYSTEM_LOG", "System Log", "Nhật ký hệ thống"),
    TELEGRAM("TELEGRAM", "Telegram", "Telegram"),
    ZALO("ZALO", "Zalo", "Zalo");

    String value;
    String name;
    String displayName;

    public static AlertChannel fromValue(String value) {
        for (var alertChannel : AlertChannel.values()) {
            if (alertChannel.getValue().equals(value)) {
                return alertChannel;
            }
        }
        throw new BWCGenericRuntimeException("Unknown Alert channel value: " + value);
    }
}
