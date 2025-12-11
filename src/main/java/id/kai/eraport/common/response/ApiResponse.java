package id.kai.eraport.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Standard API Response wrapper for all REST endpoints
 * Provides consistent response structure across the application
 *
 * @param <T> Type of the response data
 */

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    /**
     * Indicates if the request was successful
     */
    private boolean status;

    /**
     * Human-readable message describing the response
     */
    private String message;

    /**
     * The actual response data (can be null for error responses)
     */
    private T result;

    /**
     * Timestamp when the response was created
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // ==================== Pagination Fields ====================

    /**
     * Current page number (0-indexed)
     */
    private Long page;

    /**
     * Number of items per page
     */
    private Long size;

    /**
     * Total number of items across all pages
     */
    private Long total;

    /**
     * Total number of pages
     */
    private Long totalPages;

    /**
     * Indicates if there is a next page
     */
    private Boolean hasNext;

    /**
     * Indicates if there is a previous page
     */
    private Boolean hasPrevious;

    /**
     * Indicates if this is the first page
     */
    private Boolean isFirst;

    /**
     * Indicates if this is the last page
     */
    private Boolean isLast;

    // ==================== Static Factory Methods ====================

    /**
     * Create a successful response with data
     *
     * @param result Response data
     * @param message Success message
     * @return ApiResponse with status=true
     */
    public static <T> ApiResponse<T> success(T result, String message) {
        return ApiResponse.<T>builder()
                .status(true)
                .message(message)
                .result(result)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create a successful response without data
     *
     * @param message Success message
     * @return ApiResponse with status=true and null result
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .status(true)
                .message(message)
                .result(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create an error response
     *
     * @param message Error message
     * @return ApiResponse with status=false
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status(false)
                .message(message)
                .result(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create a paginated response with all pagination metadata
     *
     * @param result List of data for current page
     * @param message Success message
     * @param page Current page number
     * @param size Page size
     * @param total Total number of items
     * @param totalPages Total number of pages
     * @return ApiResponse with pagination information
     */
    public static <T> ApiResponse<T> paginated(
            T result,
            String message,
            long page,
            long size,
            long total,
            long totalPages) {

        return ApiResponse.<T>builder()
                .status(true)
                .message(message)
                .result(result)
                .page(page)
                .size(size)
                .total(total)
                .totalPages(totalPages)
                .hasNext(page < totalPages - 1)
                .hasPrevious(page > 0)
                .isFirst(page == 0)
                .isLast(page == totalPages - 1)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ==================== Convenience Methods ====================

    /**
     * Check if the response is successful
     *
     * @return true if status is true
     */
    public boolean isSuccess() {
        return this.status;
    }

    /**
     * Check if the response is an error
     *
     * @return true if status is false
     */
    public boolean isError() {
        return !this.status;
    }

    /**
     * Check if the response contains data
     *
     * @return true if result is not null
     */
    public boolean hasResult() {
        return this.result != null;
    }

    /**
     * Check if this is a paginated response
     *
     * @return true if pagination fields are present
     */
    public boolean isPaginated() {
        return this.page != null && this.size != null && this.total != null;
    }
}
