package id.kai.eraport.common.pagination;

import id.kai.eraport.dto.global.SortDirection;
import id.kai.eraport.dto.global.SortRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public class SortBuilder {
    private SortBuilder() {}

    public static Sort build(List<SortRequest> sorts) {

        if (sorts == null || sorts.isEmpty()) {
            return Sort.unsorted();
        }

        return Sort.by(
                sorts.stream()
                        .map(s -> new Sort.Order(
                                s.getDirection() == SortDirection.DESC
                                        ? Sort.Direction.DESC
                                        : Sort.Direction.ASC,
                                s.getField()
                        ))
                        .toList()
        );
    }
}
