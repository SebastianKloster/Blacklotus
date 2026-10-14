package com.kartago.blacklotus.card.enums;

public enum LayoutEnum {
    // --- CARA ÚNICA ---
    NORMAL("normal"),       // Cartas estándar
    SAGA("saga"),           // Encantamientos del tipo Saga
    LEVELER("leveler"),     // Criaturas que suben de nivel
    CLASS("class"),         // Encantamientos del tipo Clase
    CASE("case"),           // Encantamientos del tipo Caso (MKM)
    ROOM("room"),           // Encantamientos divididos tipo Habitación (DSK)

    // --- MULTI-OPCIÓN (Misma cara frontal) ---
    SPLIT("split"),         // Cartas divididas horizontales (ej: Fire // Ice)
    FLIP("flip"),           // Cartas que rotan 180° (Kamigawa)
    ADVENTURE("adventure"), // Criaturas con aventura (ej: Bonecrusher Giant)
    PREPARE("prepare"),     // Criaturas con hechizo preparado
    MUTATE("mutate"),       // Mecánica de mutar (Ikoria)
    PROTOTYPE("prototype"), // Artefactos con coste/estadísticas alternativas (BRO)

    // --- DOBLE CARA ---
    TRANSFORM("transform"), // Cartas de doble cara tradicionales (se dan vuelta)
    MODAL_DFC("modal_dfc"), // Cartas de doble cara modales (elegís qué lado jugar)
    MELD("meld"),           // Cartas que se fusionan con otra en una gigante
    BATTLE("battle"),       // Cartas de tipo Batalla / Asedio (MOM)

    // --- MANEJO DE ERRORES Y FUTURAS MECÁNICAS ---
    UNKNOWN("unknown");

    private final String value;

    LayoutEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
