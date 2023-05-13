package tech.saturdev.pakakumi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.saturdev.pakakumi.security.login.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
