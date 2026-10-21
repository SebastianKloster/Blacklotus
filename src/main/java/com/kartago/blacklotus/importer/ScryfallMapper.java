package com.kartago.blacklotus.importer;

import com.kartago.blacklotus.card.model.*;
import com.kartago.blacklotus.card.model.embedded.Legalities;
import com.kartago.blacklotus.card.model.enums.*;
import com.kartago.blacklotus.expansion.model.Expansion;
import com.kartago.blacklotus.printing.model.*;
import com.kartago.blacklotus.printing.model.embedded.*;
import com.kartago.blacklotus.printing.model.enums.FinishEnum;
import tools.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;

/** Explicit mapping keeps Scryfall field names in conversion errors. */
final class ScryfallMapper {
    private ScryfallMapper() {}

    static UUID uuid(JsonNode node, String field) {
        return field(field, () -> UUID.fromString(required(node, field)));
    }

    static UUID oracleId(JsonNode node) {
        if (!"reversible_card".equals(node.path("layout").asText())) return uuid(node, "oracle_id");
        JsonNode faces = node.path("card_faces");
        requireArray(faces, "card_faces");
        Set<UUID> ids = new HashSet<>();
        for (JsonNode face : faces) ids.add(uuid(face, "oracle_id"));
        if (ids.size() != 1) throw new IllegalArgumentException("card_faces.oracle_id=" + ids
                + ": una Printing sólo puede referenciar una Card; la carta reversible requiere adaptar esa relación");
        UUID id = ids.iterator().next();
        if (node.hasNonNull("oracle_id") && !uuid(node, "oracle_id").equals(id)) {
            throw new IllegalArgumentException("oracle_id no coincide con card_faces.oracle_id");
        }
        return id;
    }

    static Card card(JsonNode node, UUID oracleId) {
        // Reversible art variants repeat the same Oracle card on both physical sides.
        if ("reversible_card".equals(node.path("layout").asText())) {
            var canonical = node.deepCopy();
            var object = (tools.jackson.databind.node.ObjectNode) canonical;
            object.remove("card_faces");
            node.path("card_faces").get(0).properties().forEach(entry -> object.set(entry.getKey(), entry.getValue()));
            return card(object, oracleId);
        }
        LayoutEnum layout = field("layout", () -> Arrays.stream(LayoutEnum.values())
                .filter(value -> value.getValue().equals(required(node, "layout")))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("layout=" + node.path("layout") + " no contemplado en LayoutEnum")));
        Card card = new Card(null, oracleId, new ArrayList<>(), layout, new ArrayList<>(),
                required(node, "name"), manaValue(node, 0), colors(node, "color_identity"),
                node.path("game_changer").asBoolean(false), new Legalities(
                legality(node, "standard"), legality(node, "commander")));
        JsonNode faces = node.get("card_faces");
        if (faces == null) {
            card.getCardFaces().add(face(node, card, 0));
        } else {
            requireArray(faces, "card_faces");
            if (faces.isEmpty()) throw new IllegalArgumentException("card_faces: no puede estar vacío");
            for (int i = 0; i < faces.size(); i++) {
                int index = i;
                card.getCardFaces().add(field("card_faces[" + i + "]", () -> face(faces.get(index), card, index)));
            }
        }
        return card;
    }

    private static CardFace face(JsonNode node, Card card, int index) {
        return new CardFace(null, card, index, required(node, "name"), optional(node, "mana_cost"),
                manaValue(node, card.getManaValue()), colors(node, "colors"), optional(node, "type_line"),
                optional(node, "oracle_text"), optional(node, "power"), optional(node, "toughness"),
                optional(node, "loyalty"), optional(node, "defense"));
    }

    static Expansion expansion(JsonNode node, UUID id) {
        // Card released_at describes this printing, not the expansion's release date.
        return new Expansion(null, id, required(node, "set"), required(node, "set_name"), null);
    }

    static Printing printing(JsonNode node, UUID id, Card card, Expansion expansion) {
        Set<FinishEnum> finishes = EnumSet.noneOf(FinishEnum.class);
        JsonNode values = node.get("finishes");
        requireArray(values, "finishes");
        for (JsonNode value : values) {
            finishes.add(field("finishes=" + value, () -> switch (value.asText()) {
                case "nonfoil" -> FinishEnum.NON_FOIL;
                case "foil" -> FinishEnum.FOIL;
                case "etched" -> FinishEnum.ETCHED;
                default -> throw new IllegalArgumentException("acabado no contemplado en FinishEnum");
            }));
        }
        Printing printing = new Printing(null, id, card, new ArrayList<>(), expansion,
                required(node, "collector_number"), field("released_at", () -> LocalDate.parse(required(node, "released_at"))),
                finishes, new Prices(price(node, "usd"), price(node, "usd_foil"), price(node, "usd_etched")),
                optional(node, "scryfall_uri"));
        // Split/adventure cards have multiple rules faces but one physical image.
        if (node.hasNonNull("image_uris")) {
            printing.getPrintingFaces().add(new PrintingFace(null, printing, 0, images(node.get("image_uris"))));
        } else if (node.hasNonNull("card_faces")) {
            JsonNode faces = node.get("card_faces");
            requireArray(faces, "card_faces");
            for (int i = 0; i < faces.size(); i++) {
                printing.getPrintingFaces().add(new PrintingFace(null, printing, i, images(faces.get(i).path("image_uris"))));
            }
        } else {
            printing.getPrintingFaces().add(new PrintingFace(null, printing, 0, images(node.path("image_uris"))));
        }
        return printing;
    }

    private static ImageUris images(JsonNode node) {
        return new ImageUris(image(node, "thumb", "small"), image(node, "grid", "normal"),
                image(node, "display", "large"), image(node, "crop", "border_crop"), image(node, "art", "art_crop"));
    }

    private static String image(JsonNode node, String field, String fallback) {
        String value = optional(node, field);
        return value == null ? optional(node, fallback) : value;
    }

    private static BigDecimal price(JsonNode node, String key) {
        String value = optional(node.path("prices"), key);
        return value == null ? null : field("prices." + key + "=" + value, () -> new BigDecimal(value));
    }

    private static LegalityEnum legality(JsonNode node, String format) {
        return field("legalities." + format, () -> LegalityEnum.valueOf(required(node.path("legalities"), format).toUpperCase(Locale.ROOT)));
    }

    private static int manaValue(JsonNode node, int fallback) {
        JsonNode value = node.get("cmc");
        if (value == null || value.isNull()) return fallback;
        if (!value.isNumber()) throw new IllegalArgumentException("cmc=" + value + ": se esperaba un número");
        try {
            return new BigDecimal(value.asText()).intValueExact();
        } catch (ArithmeticException | NumberFormatException ex) {
            throw new IllegalArgumentException("cmc=" + value
                    + ": manaValue sólo admite valores enteros dentro del rango de int; no se trunca ni redondea", ex);
        }
    }

    private static Set<ColorEnum> colors(JsonNode node, String key) {
        Set<ColorEnum> result = EnumSet.noneOf(ColorEnum.class);
        JsonNode values = node.get(key);
        if (values == null || values.isNull()) return result;
        requireArray(values, key);
        for (JsonNode value : values) {
            result.add(field(key + "=" + value, () -> Arrays.stream(ColorEnum.values())
                    .filter(color -> color.getCode().equals(value.asText())).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("color no contemplado en ColorEnum"))));
        }
        return result;
    }

    private static void requireArray(JsonNode node, String field) {
        if (node == null || !node.isArray()) throw new IllegalArgumentException(field + ": se esperaba un array");
    }

    private static String required(JsonNode node, String key) {
        String value = optional(node, key);
        if (value == null || value.isBlank()) throw new IllegalArgumentException(key + ": campo obligatorio ausente o vacío");
        return value;
    }

    private static String optional(JsonNode node, String key) {
        JsonNode value = node.get(key);
        if (value == null || value.isNull()) return null;
        if (!value.isTextual()) throw new IllegalArgumentException(key + "=" + value + ": se esperaba texto");
        return value.asText();
    }

    private static <T> T field(String field, Supplier<T> mapping) {
        try {
            return mapping.get();
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Campo " + field + ": " + ex.getMessage(), ex);
        }
    }
}
