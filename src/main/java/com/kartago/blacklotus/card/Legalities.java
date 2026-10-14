package com.kartago.blacklotus.card;

import com.kartago.blacklotus.card.enums.LegalityEnum;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
@Embeddable
public class Legalities {
    @Enumerated(EnumType.STRING)
    private LegalityEnum standard;

    @Enumerated(EnumType.STRING)
    private LegalityEnum pioneer;

    @Enumerated(EnumType.STRING)
    private LegalityEnum modern;

    @Enumerated(EnumType.STRING)
    private LegalityEnum legacy;

    @Enumerated(EnumType.STRING)
    private LegalityEnum pauper;

    @Enumerated(EnumType.STRING)
    private LegalityEnum vintage;

    @Enumerated(EnumType.STRING)
    private LegalityEnum commander;

    @Enumerated(EnumType.STRING)
    private LegalityEnum oathbreaker;

    @Enumerated(EnumType.STRING)
    private LegalityEnum pauperCommander;

    @Enumerated(EnumType.STRING)
    private LegalityEnum duel;

    @Enumerated(EnumType.STRING)
    private LegalityEnum oldSchool;

    @Enumerated(EnumType.STRING)
    private LegalityEnum premodern;

    @Enumerated(EnumType.STRING)
    private LegalityEnum predh;

}
