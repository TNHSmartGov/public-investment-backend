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
public enum CategoryCode implements BaseEnum<String> {
    MENU_TYPE("menuType", "Menu Type"),
    PROJECT_GROUP("projectGroup", "Nhóm dự án"),
    PROJECT_KIND("projectKind", "Loại dự án"),
    CONSTRUCTION_KIND("constructionKind", "Loại công trình"),
    CONSTRUCTION_LEVEL("constructionLevel", "Cấp công trình"),
    PROJECT_LEVEL("projectLevel", "Cấp dự án"),
    PROJECT_STATUS("projectStatus", "Trạng thái dự án"),
    APPROVAL_DECISION_TYPE("approvalDecisionType", "Kiểu quyết định phê duyệt"),
    CAPITAL_PLAN_TYPE("capitalPlanType", "Loại kế hoạch vốn");

    String value;
    String displayName;

    public static CategoryCode fromValue(String value) {
        for (var category : CategoryCode.values()) {
            if (category.getValue().equals(value)) {
                return category;
            }
        }
        throw new BWCGenericRuntimeException("Unknown category code: " + value);
    }
}
