package com.kartago.blacklotus.card;

import com.kartago.blacklotus.card.enums.ColorEnum;
import com.kartago.blacklotus.card.enums.FormatEnum;
import com.kartago.blacklotus.card.enums.LegalityEnum;
import jakarta.persistence.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private UUID oracleId;

    private String name;
    private int manaValue;
    private Set<ColorEnum> colorIdentity;

    private Map<FormatEnum, LegalityEnum> legalities = new EnumMap<>(FormatEnum.class);
    private boolean isGameChanger;
}
