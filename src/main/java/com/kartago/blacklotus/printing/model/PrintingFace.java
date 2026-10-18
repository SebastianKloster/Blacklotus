package com.kartago.blacklotus.printing.model;

import com.kartago.blacklotus.printing.model.embedded.ImageUris;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_printing_face_index", // Nombre de la restricción
                        columnNames = {"printing_id", "face_index"} // Nombres de las columnas en la DB
                )
        }
)
public class PrintingFace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "printing_id", nullable = false)
    private Printing printing;

    private int faceIndex; //printing + faceIndex = unique
    @Embedded
    private ImageUris imageUris;
}
