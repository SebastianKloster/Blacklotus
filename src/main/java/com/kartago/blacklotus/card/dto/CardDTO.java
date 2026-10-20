package com.kartago.blacklotus.card.dto;

import com.kartago.blacklotus.card.model.embedded.Legalities;
import com.kartago.blacklotus.card.model.enums.ColorEnum;
import com.kartago.blacklotus.card.model.enums.LayoutEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {
    private Long id;
    private UUID oracleId;
    private List<CardFaceDTO> cardFaces;
    private LayoutEnum layout;
    private List<Long> printingIds;
    private String name;
    private double manaValue;
    private Set<ColorEnum> colorIdentity;
    private boolean isGameChanger;
    private Legalities legalities;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CardFaceDTO {
        private Long id;
        private int faceIndex;
        private String name;
        private String manaCost;
        private double manaValue;
        private Set<ColorEnum> colors;
        private String typeLine;
        private String oracleText;
        private String power;
        private String toughness;
        private String loyalty;
        private String defense;
    }
}
