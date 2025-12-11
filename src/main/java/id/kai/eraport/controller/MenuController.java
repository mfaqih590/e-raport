package id.kai.eraport.controller;

import id.kai.eraport.common.helper.ResponseBuilder;
import id.kai.eraport.common.response.ApiResponse;
import id.kai.eraport.model.Menus;
import id.kai.eraport.model.Roles;
import id.kai.eraport.service.interfaces.MenuService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/v1/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createMenu(@Valid @RequestBody Menus request,
                                                          @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "").trim();
        menuService.create(request, token);
        return ResponseBuilder.created("Menu created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateMenu(@PathVariable UUID id,
                                                          @Valid @RequestBody Menus request,
                                                          @RequestHeader("Authorization") String authHeader){

        String token = authHeader.replace("Bearer ", "").trim();
        menuService.update(id, request, token);
        return ResponseBuilder.ok("Menu updated successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Menus>>> getAllMenus(
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "Page number must be at least 1")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size must not exceed 100")
            int size,
            @RequestParam(defaultValue = "id") String sortBy ) {
        Page<Menus> menu = menuService.getAll(page-1, size, sortBy);

        return ResponseBuilder.paginated(menu, "Menu retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Menus>> getRoleById(
            @PathVariable
            UUID id) {

        Menus menus = menuService.getById(id);
        return ResponseBuilder.ok(menus, "Role retrieved successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteForm(
            @PathVariable
            UUID id,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        menuService.delete(id, token);
        return ResponseBuilder.ok("Role deleted successfully");
    }
}
