package com.kartago.blacklotus.card.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FormatEnum {
    @JsonProperty("standard") STANDARD,
    @JsonProperty("future") FUTURE,
    @JsonProperty("historic") HISTORIC,
    @JsonProperty("timeless") TIMELESS,
    @JsonProperty("gladiator") GLADIATOR,
    @JsonProperty("pioneer") PIONEER,
    @JsonProperty("explorer") EXPLORER,
    @JsonProperty("modern") MODERN,
    @JsonProperty("legacy") LEGACY,
    @JsonProperty("pauper") PAUPER,
    @JsonProperty("vintage") VINTAGE,
    @JsonProperty("penny") PENNY,
    @JsonProperty("commander") COMMANDER,
    @JsonProperty("oathbreaker") OATHBREAKER,
    @JsonProperty("standardbrawl") STANDARD_BRAWL,
    @JsonProperty("brawl") BRAWL,
    @JsonProperty("alchemy") ALCHEMY,
    @JsonProperty("paupercommander") PAUPER_COMMANDER,
    @JsonProperty("duel") DUEL,
    @JsonProperty("oldschool") OLD_SCHOOL,
    @JsonProperty("premodern") PREMODERN,
    @JsonProperty("predh") PREDH
}
