package com.kartago.blacklotus.card;

import com.kartago.blacklotus.card.enums.ColorEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private Card card;
    private int faceIndex;

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
