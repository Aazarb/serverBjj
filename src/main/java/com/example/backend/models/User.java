package com.example.backend.models;

import com.example.backend.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="app_user",uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean enabled;
    @Enumerated(EnumType.STRING) // Stocke la valeur de l'Enum en format texte
    @Column(name = "role", nullable = false)
    private RoleEnum role;

    public User(String username, String email, String password, RoleEnum role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = false;
    }

    public User(Long id, String username, String email, String password, RoleEnum role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = false;
    }
}