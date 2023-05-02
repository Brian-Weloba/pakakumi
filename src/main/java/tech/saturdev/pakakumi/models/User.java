package tech.saturdev.pakakumi.models;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @Column(nullable = false)
    private boolean enabled;

    // @Column(nullable = false)
    private String firstName;

    // @Column(nullable = false)
    private String lastName;

    // @Column(nullable = false, unique = true)
    private String email;

    // @Column(nullable = true)
    private LocalDateTime lastLogin;

    @Column(nullable = true)
    private String phoneNumber;

    // @Column(nullable = true)
    private String address;

    // @Column(nullable = true)
    private String profilePictureUrl;

}
