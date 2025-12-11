package id.kai.eraport.repository.db;

import id.kai.eraport.model.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Roles, UUID> {
    Optional<Roles> findByIdAndIsDeletedFalse(UUID id);
    Page<Roles> findAllByIsDeletedFalse(Pageable pageable);
}
