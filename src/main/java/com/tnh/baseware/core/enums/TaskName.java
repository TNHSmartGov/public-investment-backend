package com.tnh.baseware.core.enums;

import com.tnh.baseware.core.enums.base.BaseEnum;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum TaskName implements BaseEnum<String> {
    TRANSACTION_TASK("paymentTransactionTask"),
    QR_GENERATION_TASK("qrGenerationTask"),
    DASHBOARD_GENERATION_TASK("dashboardGenerationTask");

    String value;

    @Override
    public String getValue() {
        return value;
    }

}
