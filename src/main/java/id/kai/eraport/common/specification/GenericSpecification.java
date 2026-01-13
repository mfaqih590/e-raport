package id.kai.eraport.common.specification;

import id.kai.eraport.dto.global.FilterLogic;
import id.kai.eraport.dto.global.FilterRequest;
import id.kai.eraport.exception.BadRequestException;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class GenericSpecification<T> {

    public static <T> Specification<T> build(List<FilterRequest> filters) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 🔥 DEFAULT FILTER: isDeleted = false
            if (hasField(root, "isDeleted")) {
                predicates.add(cb.isFalse(root.get("isDeleted")));
            }

            if (filters == null || filters.isEmpty()) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            Predicate combined = null;

            for (FilterRequest filter : filters) {
                Predicate predicate = buildPredicate(filter, root, cb);

                if (combined == null) {
                    combined = predicate;
                } else if (filter.getLogic() == FilterLogic.OR) {
                    combined = cb.or(combined, predicate);
                } else {
                    combined = cb.and(combined, predicate);
                }
            }

            predicates.add(combined);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean hasField(Root<?> root, String fieldName) {
        try {
            root.get(fieldName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static <T> Predicate buildPredicate(
            FilterRequest filter,
            Root<T> root,
            CriteriaBuilder cb
    ) {
        Path<?> path = root.get(filter.getField());
        return switch (filter.getOperator()) {
            case EQ ->
                    cb.equal(path, cast(path.getJavaType(), filter.getValue()));

            case LIKE ->
                    cb.like(
                            cb.lower(path.as(String.class)),
                            "%" + filter.getValue().toLowerCase() + "%"
                    );

            case IN -> {
                CriteriaBuilder.In<Object> inClause = cb.in(path);
                parseInValues(path.getJavaType(), filter.getValue())
                        .forEach(inClause::value);
                yield inClause;
            }

            default ->
                    throw new IllegalArgumentException("Operator tidak didukung");
        };
    }

    private static Object cast(Class<?> type, String value) {
        if (type.equals(UUID.class)) return UUID.fromString(value);
        if (type.equals(Boolean.class)) return Boolean.valueOf(value);
        if (type.equals(Integer.class)) return Integer.valueOf(value);
        return value;
    }

    private static List<Object> parseInValues(Class<?> type, String value) {
        return Arrays.stream(value.split(","))
                .map(v -> cast(type, v.trim()))
                .toList();
    }
}
