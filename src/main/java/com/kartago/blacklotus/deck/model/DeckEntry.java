package com.kartago.blacklotus.deck.model;

import com.kartago.blacklotus.deck.model.enums.DeckZoneEnum;
import com.kartago.blacklotus.printing.model.Printing;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_deck_printing_zone", // Nombre de la restricción
                        columnNames = {"deck_id", "printing_id", "deck_zone"} // Nombres de las columnas en la DB
                )
        }
)
public class DeckEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "printing_id", nullable = false)
    private Printing printing;

    private int quantity;
    private DeckZoneEnum deckZone;
}
