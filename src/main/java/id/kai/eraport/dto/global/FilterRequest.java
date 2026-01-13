package id.kai.eraport.dto.global;

import lombok.Data;

@Data
public class FilterRequest {
    /**
     * Nama field entity (contoh: name, type, isActive)
     */
    private String field;

    /**
     * Operator filter (EQ, LIKE, GT, LT, dll)
     */
    private FilterOperator operator;

    /**
     * Nilai filter
     */
    private String value;

    /**
     * Logic antar filter (AND / OR)
     */
    private FilterLogic logic =  FilterLogic.AND; //defauld AND
}
