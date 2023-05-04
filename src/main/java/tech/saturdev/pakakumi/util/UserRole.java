package tech.saturdev.pakakumi.util;

public enum UserRole {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN"),
    ROLE_SUPER_ADMIN("SUPER_ADMIN");

    private final String role;

    UserRole(String role) {
        this.role = "ROLE_" + role;
    }

    public String getRole() {
        return role;
    }
}