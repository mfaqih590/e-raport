package id.kai.eraport.service.impl;

import id.kai.eraport.dto.auth.JwtUserInfo;
import id.kai.eraport.exception.ResourceNotFoundException;
import id.kai.eraport.model.Menus;
import id.kai.eraport.model.Roles;
import id.kai.eraport.repository.db.MenuRepository;
import id.kai.eraport.service.interfaces.JwtService;
import id.kai.eraport.service.interfaces.MenuService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private JwtService jwtService;

    public Menus create(Menus menu, String token) {
        JwtUserInfo userInfo = jwtService.extractUserInfo(token);
        menu.setCreatedBy(userInfo.getId());
        menu.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        menu.setDeleted(false);
        menu.setActive(true);

        return menuRepository.save(menu);
    }

    public Menus update(UUID id, Menus updateMenu, String token) {
        JwtUserInfo userInfo = jwtService.extractUserInfo(token);
        Menus menu = menuRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        menu.setName(updateMenu.getName());
        menu.setActive(updateMenu.isActive());
        menu.setLastModifiedAt(new Timestamp(System.currentTimeMillis()));
        menu.setLastModifiedBy(userInfo.getId());

        return menuRepository.save(menu);
    }

    public Page<Menus> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return menuRepository.findAllByIsDeletedFalse(pageable);
    }

    public Menus getById(UUID id) {
        return menuRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));
    }

    public void delete(UUID id, String token) {
        JwtUserInfo userInfo = jwtService.extractUserInfo(token);
        Menus menus = menuRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        menus.setDeleted(true);
        menus.setLastModifiedAt(new Timestamp(System.currentTimeMillis()));
        menus.setLastModifiedBy(userInfo.getId());
        menuRepository.save(menus);
    }
}
