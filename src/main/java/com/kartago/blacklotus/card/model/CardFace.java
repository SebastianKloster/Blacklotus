package com.kartago.blacklotus.card.model;

import com.kartago.blacklotus.card.model.enums.ColorEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_card_face_index", // Nombre de la restricción
                        columnNames = {"card_id", "face_index"} // Nombres de las columnas en la DB
                )
        }
)
public class CardFace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
    private int faceIndex; //Card + FaceIndex = unique

    private String name;
    private String manaCost;
    private int manaValue;
    private Set<ColorEnum> colors;
    private String typeLine;
    @Column(columnDefinition = "TEXT")
    private String oracleText;

    //Optionals
    private String power;
    private String toughness;
    private String loyalty;
    private String defense;
}
