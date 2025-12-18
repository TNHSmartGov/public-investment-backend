package com.tnh.baseware.core.specs;

import com.tnh.baseware.core.utils.LogStyleHelper;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GenericSpecification<E> implements Specification<E> {

    @Serial
    private static final long serialVersionUID = 1L;

    transient SearchRequest request;

    public static Pageable getPageable(Integer page, Integer size) {
        return PageRequest.of(Objects.requireNonNullElse(page, 0), Objects.requireNonNullElse(size, 10));
    }

    public static <T> Specification<T> distinct(Specification<T> specification) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return specification.toPredicate(root, query, criteriaBuilder);
        };
    }

    @Override
    @NonNull
    public Specification<E> and(Specification<E> other) {
        return Specification.super.and(other);
    }

    @Override
    @NonNull
    public Specification<E> or(Specification<E> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(@NonNull Root<E> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb) {
        var predicate = cb.equal(cb.literal(Boolean.TRUE), Boolean.TRUE);

        for (var filter : this.request.getFilters()) {
            log.debug(LogStyleHelper.debug("Filter: {} {} {}"), filter.getKey(), filter.getOperator().toString(),
                    filter.getValue());
            predicate = filter.getOperator().build(root, cb, filter, predicate);
        }

        var orders = new ArrayList<Order>();
        for (var sort : this.request.getSorts()) {
            orders.add(sort.getDirection().build(root, cb, sort));
        }

        query.orderBy(orders);
        return predicate;
    }
}
