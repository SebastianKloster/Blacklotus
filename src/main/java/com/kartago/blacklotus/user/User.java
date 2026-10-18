package com.kartago.blacklotus.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String googleSubject;

    @Column(unique = true, nullable = false)
    private String email;

    private String nickname;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    // Se ejecuta automáticamente antes de insertar el registro por primera vez
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
