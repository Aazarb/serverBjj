package com.example.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(unique = true, nullable = false)
    private String token;
    @Column(nullable = false)
    private Date creationDate;
    @Column(nullable = false)
    private Date expirationDate;
    @Column
    private Date confirmationDate;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}