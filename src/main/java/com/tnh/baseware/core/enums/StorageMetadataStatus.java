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
public enum StorageMetadataStatus implements BaseEnum<String> {
    ACTIVE("ACTIVE", "active", "Hoạt động"),
    FULL("FULL", "full", "Đầy"),
    ERROR("ERROR", "error", "Lỗi"),
    SYNC_PENDING("SYNC_PENDING", "sync_pending", "Đang đồng bộ"),
    SYNCING("SYNCING", "syncing", "Đang đồng bộ"),
    OFFLINE("OFFLINE", "offline", "Ngoại tuyến");

    String value;
    String name;
    String displayName;

    public static StorageMetadataStatus fromValue(String value) {
        for (StorageMetadataStatus type : StorageMetadataStatus.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new BWCGenericRuntimeException("Unknown value: " + value);
    }
}
