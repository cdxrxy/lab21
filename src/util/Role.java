package util;

public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    Role(String role) {
        this.role = role;
    }

    private final String role;
    public String getRole() {
        return role;
    }
}
