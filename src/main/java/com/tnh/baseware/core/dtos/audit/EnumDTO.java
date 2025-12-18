package com.tnh.baseware.core.dtos.audit;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnumDTO<E> {
    E value;
    String name;
    String displayName;
}
