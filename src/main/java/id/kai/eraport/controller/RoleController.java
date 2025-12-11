package id.kai.eraport.controller;

import id.kai.eraport.common.helper.ResponseBuilder;
import id.kai.eraport.common.response.ApiResponse;
import id.kai.eraport.model.Roles;
import id.kai.eraport.service.interfaces.RoleService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/v1/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(@Validated @RequestBody Roles request,
                                                      @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        roleService.create(request, token);
        return ResponseBuilder.created("Role created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> update(@PathVariable UUID id,
                                                      @Validated @RequestBody Roles request,
                                                      @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        roleService.update(id, request, token);
        return ResponseBuilder.ok("Role updated successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Roles>>> getAllRole(
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "Page number must be at least 1")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size must not exceed 100")
            int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Page<Roles> role = roleService.getAll(page - 1, size, sortBy);
        return ResponseBuilder.paginated(role, "Role retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Roles>> getRoleById(
            @PathVariable
            UUID id) {

        Roles role = roleService.getById(id);
        return ResponseBuilder.ok(role, "Role retrieved successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteForm(
            @PathVariable
            UUID id,
            @RequestHeader("Authorization")  String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        roleService.delete(id, token);
        return ResponseBuilder.ok("Role deleted successfully");
    }
}
