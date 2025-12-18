package com.tnh.baseware.core.enums;

import com.tnh.baseware.core.enums.base.BaseEnum;
import com.tnh.baseware.core.exceptions.BWCGenericRuntimeException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.stream.Stream;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum Operator implements BaseEnum<String> {
    EQUAL("eq"),
    NOT_EQUAL("neg"),
    GREATER_THAN("gt"),
    GREATER_THAN_OR_EQUAL_TO("gte"),
    LESS_THAN("lt"),
    LESS_THAN_OR_EQUAL_TO("lte"),
    IN("in"),
    NOT_IN("nin"),
    BETWEEN("btn"),
    CONTAINS("like");

    String value;

    public static Operator fromValue(String value) {
        return Stream.of(Operator.values())
                .filter(operator -> operator.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new BWCGenericRuntimeException("Unknown operator: " + value));
    }
}
