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

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // // @Autowired
    // // public UserService(
    // // UserRepository userRepository,
    // // RoleRepository roleRepository,
    // // BCryptPasswordEncoder passwordEncoder) {
    // // this.userRepository = userRepository;
    // // this.roleRepository = roleRepository;
    // // this.passwordEncoder = passwordEncoder;
    // // }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        Role userRole = roleRepository.findByRole("ROLE_ADMIN");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }
}
