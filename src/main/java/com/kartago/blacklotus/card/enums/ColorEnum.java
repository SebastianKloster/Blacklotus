package com.kartago.blacklotus.card.enums;

public enum ColorEnum {
    // PASO 1: Aquí se definen los valores.
    // Al escribir ("W"), Java automáticamente ejecuta el constructor pasándole "W".
    WHITE("W"),
    BLUE("U"),
    BLACK("B"),
    RED("R"),
    GREEN("G");

    // PASO 3: Aquí se almacena el valor para siempre (es final, no cambia)
    private final String code;

    // PASO 2: Este es el constructor que Java ejecuta internamente para cada elemento
    ColorEnum(String code) {
        this.code = code; // Aquí "W" se guarda dentro de WHITE, "U" dentro de BLUE, etc.
    }

    public String getCode() {
        return code;
    }
}
