package dev.jossegonnza.personal_finance_manager.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Category {

    private final UUID id;
    private final UUID userId;
    private final String name;
    private final CategoryKind kind;
    private final String colorHex;

    public Category(UUID userId, String name, CategoryKind kind, String colorHex) {
        String normalizedName = name == null ? null : name.trim();
        if (normalizedName == null || normalizedName.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        this.id = UUID.randomUUID();
        this.userId = Objects.requireNonNull(userId, "userId cannot be null");
        this.name = normalizedName;
        this.kind = Objects.requireNonNull(kind, "categoryKind cannot be null");
        this.colorHex = Objects.requireNonNull(colorHex, "colorHex cannot be null");
    }

    public Category(UUID id, UUID userId, String name, CategoryKind kind, String colorHex) {
        String normalizedName = name == null ? null : name.trim();
        if (normalizedName == null || normalizedName.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }

        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.userId = Objects.requireNonNull(userId, "userId cannot be null");
        this.kind = Objects.requireNonNull(kind, "kind cannot be null");
        this.colorHex = Objects.requireNonNull(colorHex, "colorHex cannot be null");
        this.name = normalizedName;
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

    public String colorHex() {
        return colorHex;
    }
}
