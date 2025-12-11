package id.kai.eraport.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "menus")
public class Menus {
    @Id
    @GeneratedValue
    @org.hibernate.annotations.GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "icon")
    private String icon;

    @Column(name = "type")
    private String type;

    @Column(name = "parent_menu_id")
    private UUID parentMenuId;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "last_modified_at")
    private Timestamp lastModifiedAt;

    @Column(name = "last_modified_by")
    private UUID lastModifiedBy;
}
