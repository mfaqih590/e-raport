package id.kai.eraport.service.interfaces;

import id.kai.eraport.model.Roles;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    Roles create(Roles role, String token);
    Roles update(UUID id, Roles updatedRole, String token);
    Page<Roles> getAll(int page, int size, String sortBy);
    Roles getById(UUID id);
    void delete(UUID id, String token);
}
