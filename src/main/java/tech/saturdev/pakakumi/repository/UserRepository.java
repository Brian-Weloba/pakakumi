package tech.saturdev.pakakumi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.saturdev.pakakumi.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}