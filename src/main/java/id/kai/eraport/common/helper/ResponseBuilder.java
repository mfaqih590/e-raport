package id.kai.eraport.common.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import id.kai.eraport.common.response.ApiResponse;

import java.util.List;

/**
 * Utility class for building consistent API responses
 * Provides static methods to create standardized ResponseEntity objects
 */
public class ResponseBuilder {
    private static final Logger log = LoggerFactory.getLogger(ResponseBuilder.class);

    private ResponseBuilder() {
        // Private constructor to prevent instantiation
    }

    // ==================== SUCCESS RESPONSES ====================

    /**
     * Build successful response with data
     *
     * @param data    Response data
     * @param message Success message
     * @return ResponseEntity with HTTP 200 OK
     */
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        log.info("✅ Success: {}", message);
        return ResponseEntity.ok(ApiResponse.success(data, message));
    }

    /**
     * Build successful response without data
     *
     * @param message Success message
     * @return ResponseEntity with HTTP 200 OK
     */
    public static ResponseEntity<ApiResponse<Object>> ok(String message) {
        log.info("✅ Success: {}", message);
        return ResponseEntity.ok(ApiResponse.success(null, message));
    }

    /**
     * Build created response (HTTP 201)
     *
     * @param data    Created resource data
     * @param message Success message
     * @return ResponseEntity with HTTP 201 CREATED
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        log.info("✅ Created: {}", message);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(data, message));
    }

    /**
     * Build created response without data
     *
     * @param message Success message
     * @return ResponseEntity with HTTP 201 CREATED
     */
    public static ResponseEntity<ApiResponse<Object>> created(String message) {
        log.info("✅ Created: {}", message);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, message));
    }

    /**
     * Build accepted response (HTTP 202) for async operations
     *
     * @param message Acceptance message
     * @return ResponseEntity with HTTP 202 ACCEPTED
     */
    public static ResponseEntity<ApiResponse<Object>> accepted(String message) {
        log.info("✅ Accepted: {}", message);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success(null, message));
    }

    /**
     * Build no content response (HTTP 204)
     * Used for successful operations that return no data
     *
     * @return ResponseEntity with HTTP 204 NO CONTENT
     */
    public static ResponseEntity<Void> noContent() {
        log.info("✅ No content response");
        return ResponseEntity.noContent().build();
    }

    // ==================== PAGINATED RESPONSES ====================

    /**
     * Build paginated response with Page object
     * Page number starts from 1 (not 0)
     *
     * @param pageData Spring Data Page object
     * @param message  Success message
     * @return ResponseEntity with paginated data
     */
    public static <T> ResponseEntity<ApiResponse<List<T>>> paginated(Page<T> pageData, String message) {
        log.info("✅ Paginated response: {} (page: {}, size: {}, total: {})",
                message, pageData.getNumber() + 1, pageData.getSize(), pageData.getTotalElements());

        long currentPage = pageData.getNumber() + 1; // Convert 0-indexed to 1-indexed
        long totalPages = pageData.getTotalPages();

        ApiResponse<List<T>> response = ApiResponse.<List<T>>builder()
                .status(true)
                .message(message)
                .result(pageData.getContent())
                .page(currentPage)
                .size((long) pageData.getSize())
                .total(pageData.getTotalElements())
                .totalPages(totalPages)
                .hasNext(currentPage < totalPages)
                .hasPrevious(currentPage > 1)
                .isFirst(currentPage == 1)
                .isLast(currentPage == totalPages || totalPages == 0)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Build paginated response with custom total count
     * Page number starts from 1 (not 0)
     *
     * @param pageData Spring Data Page object
     * @param total    Custom total count
     * @param message  Success message
     * @return ResponseEntity with paginated data
     */
    public static <T> ResponseEntity<ApiResponse<List<T>>> paginated(Page<T> pageData, long total, String message) {
        log.info("✅ Paginated response: {} (page: {}, size: {}, total: {})",
                message, pageData.getNumber() + 1, pageData.getSize(), total);

        long currentPage = pageData.getNumber() + 1; // Convert 0-indexed to 1-indexed
        long totalPages = pageData.getTotalPages();

        ApiResponse<List<T>> response = ApiResponse.<List<T>>builder()
                .status(true)
                .message(message)
                .result(pageData.getContent())
                .page(currentPage)
                .size((long) pageData.getSize())
                .total(total)
                .totalPages(totalPages)
                .hasNext(currentPage < totalPages)
                .hasPrevious(currentPage > 1)
                .isFirst(currentPage == 1)
                .isLast(currentPage == totalPages || totalPages == 0)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Build paginated response from list with manual pagination info
     * Page number starts from 1 (not 0)
     *
     * @param data    List of data
     * @param page    Current page number (1-indexed)
     * @param size    Page size
     * @param total   Total elements
     * @param message Success message
     * @return ResponseEntity with paginated data
     */
    public static <T> ResponseEntity<ApiResponse<List<T>>> paginated(
            List<T> data, int page, int size, long total, String message) {

        long totalPages = size > 0 ? (long) Math.ceil((double) total / size) : 0;
        log.info("✅ Paginated response: {} (page: {}, size: {}, total: {})",
                message, page, size, total);

        ApiResponse<List<T>> response = ApiResponse.<List<T>>builder()
                .status(true)
                .message(message)
                .result(data)
                .page((long) page)
                .size((long) size)
                .total(total)
                .totalPages(totalPages)
                .hasNext(page < totalPages)
                .hasPrevious(page > 1)
                .isFirst(page == 1)
                .isLast(page == totalPages || totalPages == 0)
                .build();

        return ResponseEntity.ok(response);
    }

    // ==================== ERROR RESPONSES ====================

    /**
     * Build error response from Exception
     *
     * @param e Exception object
     * @return ResponseEntity with HTTP 500 INTERNAL SERVER ERROR
     */
    public static ResponseEntity<ApiResponse<Object>> error(Exception e) {
        log.error("❌ Internal error: {}", e.getMessage(), e);
        return error("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Build error response with custom message and status
     *
     * @param message Error message
     * @param status  HTTP status code
     * @return ResponseEntity with specified status
     */
    public static ResponseEntity<ApiResponse<Object>> error(String message, HttpStatus status) {
        if (status.is5xxServerError()) {
            log.error("❌ Error ({}): {}", status.value(), message);
        } else {
            log.warn("⚠️ Client error ({}): {}", status.value(), message);
        }

        ApiResponse<Object> response = ApiResponse.builder()
                .status(false)
                .message(message)
                .result(null)
                .build();

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Build bad request error response (HTTP 400)
     *
     * @param message Error message
     * @return ResponseEntity with HTTP 400 BAD REQUEST
     */
    public static ResponseEntity<ApiResponse<Object>> badRequest(String message) {
        log.warn("⚠️ Bad request: {}", message);
        return error(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Build unauthorized error response (HTTP 401)
     *
     * @param message Error message
     * @return ResponseEntity with HTTP 401 UNAUTHORIZED
     */
    public static ResponseEntity<ApiResponse<Object>> unauthorized(String message) {
        log.warn("⚠️ Unauthorized: {}", message);
        return error(message, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Build forbidden error response (HTTP 403)
     *
     * @param message Error message
     * @return ResponseEntity with HTTP 403 FORBIDDEN
     */
    public static ResponseEntity<ApiResponse<Object>> forbidden(String message) {
        log.warn("⚠️ Forbidden: {}", message);
        return error(message, HttpStatus.FORBIDDEN);
    }

    /**
     * Build not found error response (HTTP 404)
     *
     * @param message Error message
     * @return ResponseEntity with HTTP 404 NOT FOUND
     */
    public static ResponseEntity<ApiResponse<Object>> notFound(String message) {
        log.warn("⚠️ Not found: {}", message);
        return error(message, HttpStatus.NOT_FOUND);
    }

    /**
     * Build conflict error response (HTTP 409)
     * Used for duplicate entries or constraint violations
     *
     * @param message Error message
     * @return ResponseEntity with HTTP 409 CONFLICT
     */
    public static ResponseEntity<ApiResponse<Object>> conflict(String message) {
        log.warn("⚠️ Conflict: {}", message);
        return error(message, HttpStatus.CONFLICT);
    }

    /**
     * Build unprocessable entity error response (HTTP 422)
     * Used for validation errors
     *
     * @param message Error message
     * @return ResponseEntity with HTTP 422 UNPROCESSABLE ENTITY
     */
    public static ResponseEntity<ApiResponse<Object>> unprocessableEntity(String message) {
        log.warn("⚠️ Unprocessable entity: {}", message);
        return error(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Build internal server error response (HTTP 500)
     *
     * @param message Error message
     * @return ResponseEntity with HTTP 500 INTERNAL SERVER ERROR
     */
    public static ResponseEntity<ApiResponse<Object>> internalError(String message) {
        log.error("❌ Internal server error: {}", message);
        return error(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Build service unavailable error response (HTTP 503)
     *
     * @param message Error message
     * @return ResponseEntity with HTTP 503 SERVICE UNAVAILABLE
     */
    public static ResponseEntity<ApiResponse<Object>> serviceUnavailable(String message) {
        log.error("❌ Service unavailable: {}", message);
        return error(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
