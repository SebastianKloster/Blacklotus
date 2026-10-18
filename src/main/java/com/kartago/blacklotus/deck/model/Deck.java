package com.kartago.blacklotus.deck.model;

import com.kartago.blacklotus.card.model.enums.FormatEnum;
import com.kartago.blacklotus.location.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Location location;

    private FormatEnum format;

    private boolean isFavorite;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;
    private LocalDateTime updatedAt;

    // Se ejecuta automáticamente antes de insertar el registro por primera vez
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
}
