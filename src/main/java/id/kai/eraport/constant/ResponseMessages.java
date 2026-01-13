package id.kai.eraport.constant;

public class ResponseMessages {
    private ResponseMessages() {
    } // Prevent instantiation

    // Authentication
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String REFRESH_TOKEN_SUCCESS = "Token refreshed successfully";
    public static final String LOGOUT_SUCCESS = "Logged out successfully";

    // User Management
    public static final String REGISTER_SUCCESS = "User registered successfully";
    public static final String USER_PROFILE_RETRIEVED = "User profile retrieved successfully";
    public static final String USER_RETRIEVED = "User retrieved successfully";
    public static final String USER_UPDATE_SUCCESS = "User profile updated successfully";
    public static final String PASSWORD_CHANGE_SUCCESS = "Password changed successfully";
    public static final String PRODUCT_CREATED = "Create product successfully";
    public static final String PRODUCT_LIST_RETRIEVED = "Product list retrieved successfully";

    // Errors
    public static final String VALIDATION_FAILED = "Validation failed";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred";

    //success
    public static final String SUCCESS = "Success";
}
