package id.kai.eraport.service.interfaces;

import id.kai.eraport.model.Menus;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MenuService {
    Menus create(Menus menu, String token);
    Menus update(UUID id, Menus updateMenu, String token);
    Page<Menus> getAll(int page, int size, String sortBy);
    Menus getById(UUID id);
    void delete(UUID id, String token);
}
