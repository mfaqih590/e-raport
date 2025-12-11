package id.kai.eraport.service.impl;

import id.kai.eraport.dto.auth.JwtUserInfo;
import id.kai.eraport.exception.ResourceNotFoundException;
import id.kai.eraport.model.Roles;
import id.kai.eraport.repository.db.RoleRepository;
import id.kai.eraport.service.interfaces.JwtService;
import id.kai.eraport.service.interfaces.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;

    public Roles create(Roles role, String token) {
        JwtUserInfo userInfo = jwtService.extractUserInfo(token);
        role.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        role.setCreatedBy(userInfo.getId());
        role.setDeleted(false);
        return roleRepository.save(role);
    }

    public Roles update(UUID id, Roles updatedRole, String token) {
        JwtUserInfo userInfo = jwtService.extractUserInfo(token);
        Roles role = roleRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setRoleName(updatedRole.getRoleName());
        role.setLastModifiedAt(new Timestamp(System.currentTimeMillis()));
        role.setLastModifiedBy(userInfo.getId());

        return roleRepository.save(role);
    }

    public Page<Roles> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return roleRepository.findAllByIsDeletedFalse(pageable);
    }

    public Roles getById(UUID id) {
        return roleRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    public void delete(UUID id, String token) {
        JwtUserInfo userInfo = jwtService.extractUserInfo(token);
        Roles role = roleRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        role.setDeleted(true);
        role.setLastModifiedAt(new Timestamp(System.currentTimeMillis()));

        roleRepository.save(role);
    }
}
