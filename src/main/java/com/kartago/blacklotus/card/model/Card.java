package com.kartago.blacklotus.card.model;

import com.kartago.blacklotus.card.model.embedded.Legalities;
import com.kartago.blacklotus.card.model.enums.ColorEnum;
import com.kartago.blacklotus.card.model.enums.LayoutEnum;
import com.kartago.blacklotus.printing.model.Printing;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        indexes = {
                @Index(name = "idx_card_name", columnList = "name"),
                @Index(
                        name = "idx_card_commander_name",
                        columnList = "commander, name"
                ),
                @Index(
                        name = "idx_card_standard_name",
                        columnList = "standard, name"
                )
        }
)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private UUID oracleId;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardFace> cardFaces = new ArrayList<>();
    // Existing databases store ordinals. Append new layouts; do not reorder them.
    @Enumerated(EnumType.ORDINAL)
    private LayoutEnum layout;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Printing> printings = new ArrayList<>();

    private String name;
    private double manaValue;
    private Set<ColorEnum> colorIdentity;


    private boolean isGameChanger;
    @Embedded
    private Legalities legalities;



    public void addPrinting(Printing printing) {
        printings.add(printing);
        printing.setCard(this);
    }
}
