package com.tnh.baseware.core.specs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    List<FilterRequest> filters;
    List<SortRequest> sorts;
    Integer page;
    Integer size;

    public List<FilterRequest> getFilters() {
        if (Objects.isNull(this.filters))
            return new ArrayList<>();
        return this.filters;
    }

    public boolean hasFilters(String filterName) {
        return this.filters != null && this.filters.stream().anyMatch(filter -> filter.getKey().equals(filterName));
    }

    public List<SortRequest> getSorts() {
        if (Objects.isNull(this.sorts))
            return new ArrayList<>();
        return this.sorts;
    }
}
