package id.kai.eraport.dto.global;

import lombok.Data;

@Data
public class SortRequest {
    private String field;
    private SortDirection direction;
}
