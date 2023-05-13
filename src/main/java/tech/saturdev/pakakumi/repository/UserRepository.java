package tech.saturdev.pakakumi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.saturdev.pakakumi.security.login.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUserName(String userName);
}
