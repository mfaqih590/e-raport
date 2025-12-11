package id.kai.eraport.repository.db;

import id.kai.eraport.model.Menus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<Menus, UUID> {
    Optional<Menus> findByIdAndIsDeletedFalse(UUID id);
    Page<Menus> findAllByIsDeletedFalse(Pageable pageable);
    Page<Menus> findAllByParentMenuIdAndIsDeletedFalse(UUID parentMenuId, Pageable pageable);
}
