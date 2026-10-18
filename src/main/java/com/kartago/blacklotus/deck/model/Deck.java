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
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false, unique = true)
    private Location location;

    private FormatEnum format;

    private boolean isFavorite;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;
    private LocalDateTime updatedAt;

    private String description;

    private int version; //Optimistic Locking
    
    @Setter(lombok.AccessLevel.NONE)
    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeckEntry> entries = new ArrayList<>();

    public void addEntry(DeckEntry entry) {
        entries.add(entry);
        entry.setDeck(this);
    }

    public void removeEntry(DeckEntry entry) {
        if (entries.remove(entry)) {
            entry.setDeck(null);
        }
    }

    // Se ejecuta automáticamente antes de insertar el registro por primera vez
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
}
