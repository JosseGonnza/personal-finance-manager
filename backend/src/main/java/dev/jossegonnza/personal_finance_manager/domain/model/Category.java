package dev.jossegonnza.personal_finance_manager.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Category {

    private final UUID id;
    private final UUID userId;
    private final String name;
    private final CategoryKind kind;
    private final CategoryColor color;

    public Category(UUID userId,
                    String name,
                    CategoryKind kind,
                    CategoryColor color) {

        String normalizedName = name == null ? null : name.trim();
        if (normalizedName == null || normalizedName.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }

        this.id = UUID.randomUUID();
        this.userId = Objects.requireNonNull(userId, "userId cannot be null");
        this.name = normalizedName;
        this.kind = Objects.requireNonNull(kind, "kind cannot be null");
        this.color = Objects.requireNonNull(color, "color cannot be null");
    }

    public Category(UUID id,
                    UUID userId,
                    String name,
                    CategoryKind kind,
                    CategoryColor color) {
        String normalizedName = name == null ? null : name.trim();
        if (normalizedName == null || normalizedName.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }

        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.userId = Objects.requireNonNull(userId, "userId cannot be null");
        this.name = normalizedName;
        this.kind = Objects.requireNonNull(kind, "kind cannot be null");
        this.color = Objects.requireNonNull(color, "color cannot be null");
    }

    public Category update(String name,
                           CategoryKind kind,
                           CategoryColor color) {

        String normalizedName = name == null ? null : name.trim();
        if (normalizedName == null || normalizedName.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }

        return new Category(
                this.id,
                this.userId,
                normalizedName,
                Objects.requireNonNull(kind, "kind cannot be null"),
                Objects.requireNonNull(color, "color cannot be null")
        );
    }

    public UUID id() {
        return id;
    }

    public UUID userId() {
        return userId;
    }

    public String name() {
        return name;
    }

    public CategoryKind kind() {
        return kind;
    }

    public CategoryColor color() {
        return color;
    }
}
