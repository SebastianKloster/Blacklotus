package com.kartago.blacklotus.card.model;

import com.kartago.blacklotus.card.model.enums.ColorEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CardFace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
    private int faceIndex; //Card + FaceIndex = unique

    private String name;
    private String manaCost;
    private int manaValue;
    private Set<ColorEnum> colors;
    private String typeLine;
    private String oracleText;

    //Optionals
    private String power;
    private String toughness;
    private String loyalty;
    private String defense;
}
