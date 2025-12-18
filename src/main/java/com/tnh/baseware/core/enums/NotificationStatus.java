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
public enum NotificationStatus implements BaseEnum<String> {
    DRAFT("DRAFT", "active", "Đang soạn thảo"),
    SENT("SENT", "inactive", "Đã gửi"),
    EXPIRED("EXPIRED", "canceled", "Đã hết hạn");

    String value;
    String name;
    String displayName;

    public static ServicePackageStatus fromValue(String value) {
        for (ServicePackageStatus type : ServicePackageStatus.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new BWCGenericRuntimeException("Unknown value: " + value);
    }
}
