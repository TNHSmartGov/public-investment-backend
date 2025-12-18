package com.tnh.baseware.core.entities.audit;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagedModel<T> extends RepresentationModel<PagedModel<T>> {

    List<T> content;
    Long totalElements;
    Integer totalPages;
    Integer number;
    Integer size;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
