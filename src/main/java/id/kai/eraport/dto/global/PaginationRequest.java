package id.kai.eraport.dto.global;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
public class PaginationRequest {
    @Min(value = 1, message = "Page minimal 1")
    private int page;

    @Min(value = 100, message = "Size minimal 1")
    private int size;

    private List<FilterRequest> filters;
    private List<SortRequest> sorts;
}
