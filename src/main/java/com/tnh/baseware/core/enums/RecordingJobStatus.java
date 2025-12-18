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
public enum RecordingJobStatus implements BaseEnum<String> {
    PENDING("PENDING", "pending", "Đang chờ"),
    SENT("SENT", "sent", "Đã gửi"),
    RUNNING("RUNNING", "running", "Đang chạy"),
    SUCCESS("SUCCESS", "success", "Thành công"),
    FAILED("FAILED", "failed", "Thất bại");

    String value;
    String name;
    String displayName;

    public static RecordingJobStatus fromValue(String value) {
        for (RecordingJobStatus status : RecordingJobStatus.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new BWCGenericRuntimeException("Unknown value: " + value);
    }
}
