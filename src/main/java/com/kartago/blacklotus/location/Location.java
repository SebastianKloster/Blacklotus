package com.kartago.blacklotus.location;

import com.kartago.blacklotus.cardcopy.CardCopy;
import com.kartago.blacklotus.deck.model.DeckEntry;
import com.kartago.blacklotus.location.enums.LocationTypeEnum;
import com.kartago.blacklotus.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_name", // Nombre de la restricción
                        columnNames = {"user_id", "name"} // Nombres de las columnas en la DB
                )
        }
)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;

    @Enumerated(EnumType.STRING)
    private LocationTypeEnum locationType;
}
