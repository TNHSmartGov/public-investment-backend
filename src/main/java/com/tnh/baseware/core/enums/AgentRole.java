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
public enum AgentRole implements BaseEnum<String> {
    SALES_AGENT("SALES", "Sales", "Nhân viên bán gói"),
    BILLING_AGENT("BILLING", "Billing", "Nhân viên thu cước"),
    TECHNICAL_AGENT("TECHNICAL", "Technical", "Nhân viên kỹ thuật"),
    SUPPORT_AGENT("SUPPORT", "Support", "Nhân viên CSKH");

    String value;
    String name;
    String displayName;

    public static AgentRole fromValue(String value) {
        for (AgentRole role : AgentRole.values()) {
            if (role.getValue().equals(value)) {
                return role;
            }
        }
        throw new BWCGenericRuntimeException("Unknown AgentRole value: " + value);
    }
}
