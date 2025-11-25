package dev.jossegonnza.personal_finance_manager.domain.model;

public enum CategoryColor {
    RED("#EF4444"),
    ORANGE("#F97316"),
    YELLOW("#EAB308"),
    GREEN("#22C55E"),
    BLUE("#3B82F6"),
    PURPLE("#A855F7"),
    PINK("#EC4899"),
    GRAY("#6B7280"),
    TEAL("#14B8A6"),
    BROWN("#8B5E3C");

    private final String hex;

    CategoryColor(String hex) {
        this.hex = hex;
    }

    public String hex() {
        return hex;
    }
}
