package tech.saturdev.pakakumi.service;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import tech.saturdev.pakakumi.repository.RoleRepository;
import tech.saturdev.pakakumi.repository.UserRepository;
import tech.saturdev.pakakumi.security.login.models.Role;
import tech.saturdev.pakakumi.security.login.models.User;
import tech.saturdev.pakakumi.util.PasswordMismatchException;
import tech.saturdev.pakakumi.util.UserRole;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public User saveUser(User user) throws PasswordMismatchException {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (!passwordEncoder.matches(user.getMatchingPassword(), user.getPassword())) {
            throw new PasswordMismatchException("Passwords don't match");
        }

        user.setEnabled(true);
        Role userRole = roleRepository.findByRole(UserRole.ROLE_USER.getRole());
        if (userRole == null) {
            userRole = Role.builder()
                    .role(UserRole.ROLE_USER.getRole())
                    .build();
            roleRepository.save(userRole);
        }
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    public User makeAdmin(String username) {
        User user = userRepository.findByUserName(username);
        if (user != null) {
            Role adminRole = roleRepository.findByRole(UserRole.ROLE_ADMIN.getRole());
            if (adminRole == null) {
                adminRole = Role.builder()
                        .role(UserRole.ROLE_ADMIN.getRole())
                        .build();
                roleRepository.save(adminRole);
            }
            user.getRoles().add(adminRole);
            return userRepository.save(user);
        }
        return null;
    }

    public User makeSuperAdmin(String username) {
        User user = userRepository.findByUserName(username);
        if (user != null) {
            Role superAdminRole = roleRepository.findByRole(UserRole.ROLE_SUPER_ADMIN.getRole());
            if (superAdminRole == null) {
                superAdminRole = Role.builder()
                        .role(UserRole.ROLE_SUPER_ADMIN.getRole())
                        .build();
                roleRepository.save(superAdminRole);
            }
            user.getRoles().add(superAdminRole);
            return userRepository.save(user);
        }
        return null;
    }

}
