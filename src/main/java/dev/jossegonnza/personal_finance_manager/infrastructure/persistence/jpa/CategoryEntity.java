package dev.jossegonnza.personal_finance_manager.infrastructure.persistence.jpa;

import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "kind", nullable = false, length = 20)
    private CategoryKind kind;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false, length = 30)
    private CategoryColor color;

    protected CategoryEntity() {
    }

    public CategoryEntity(UUID id,
                          UUID userId,
                          String name,
                          CategoryKind kind,
                          CategoryColor color) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.kind = kind;
        this.color = color;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryKind getKind() {
        return kind;
    }

    public void setKind(CategoryKind kind) {
        this.kind = kind;
    }

    public CategoryColor getColor() {
        return color;
    }

    public void setColor(CategoryColor color) {
        this.color = color;
    }
}
