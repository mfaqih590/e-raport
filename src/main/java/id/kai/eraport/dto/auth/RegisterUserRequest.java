package id.kai.eraport.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Pattern(regexp = ".*@.*", message = "Email must contain '@'")
    private String email;
    @NotBlank(message = "Email is required")
    private String password;
    @NotNull(message = "Role is required")
    private UUID role;
}
