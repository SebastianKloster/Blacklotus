package com.kartago.blacklotus.card.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum LegalityEnum {
    @JsonProperty("legal") LEGAL,
    @JsonProperty("not_legal") NOT_LEGAL,
    @JsonProperty("restricted") RESTRICTED,
    @JsonProperty("banned") BANNED
}
