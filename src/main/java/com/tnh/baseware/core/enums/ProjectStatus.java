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
public enum ProjectStatus implements BaseEnum<String> {

    PLANNING("PLANNING", "Planning", "Đang lập kế hoạch"),
    NEW("NEW", "New", "Dự án mới"),
    IN_PROGRESS("IN_PROGRESS", "In Progress", "Đang thực hiện"),
    COMPLETED("COMPLETED", "Completed", "Đã hoàn thành");

    String value;
    String name;
    String displayName;

    public static ProjectStatus fromValue(String value) {
        for (var status : ProjectStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new BWCGenericRuntimeException("Unknown ProjectStatus value: " + value);
    }
}
