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
public enum ActionType implements BaseEnum<String> {

    INSERT("INSERT", "Insert", "Thêm mới"),
    UPDATE("UPDATE", "Update", "Cập nhật"),
    DELETE("DELETE", "Delete", "Xóa");

    String value;
    String name;
    String displayName;

    public static ActionType fromValue(String value) {
        for (var type : ActionType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new BWCGenericRuntimeException("Unknown ActionType value: " + value);
    }
}
