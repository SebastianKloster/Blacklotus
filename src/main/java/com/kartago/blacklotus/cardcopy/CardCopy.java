package com.kartago.blacklotus.cardcopy;

import com.kartago.blacklotus.deck.model.enums.DeckZoneEnum;
import com.kartago.blacklotus.location.Location;
import com.kartago.blacklotus.printing.Printing;
import com.kartago.blacklotus.printing.enums.FinishEnum;
import com.kartago.blacklotus.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CardCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private User user;
    private Printing printing;
    private Location location;

    private DeckZoneEnum deckZone;

    private LocalDate acquiredAt;

    private FinishEnum finish;
    private boolean isProxy;

    private int version; //Optimistic Locking

}
