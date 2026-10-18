package com.kartago.blacklotus.deck.model;

import com.kartago.blacklotus.deck.model.enums.DeckZoneEnum;
import com.kartago.blacklotus.printing.Printing;
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
