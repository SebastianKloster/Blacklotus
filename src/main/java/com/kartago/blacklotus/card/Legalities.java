package com.kartago.blacklotus.card;

import com.kartago.blacklotus.card.enums.LegalityEnum;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Legalities {
    @Enumerated(EnumType.STRING)
    private LegalityEnum standard;
    @Enumerated(EnumType.STRING)
    private LegalityEnum commander;
}
