package com.kartago.blacklotus.printing;

import com.kartago.blacklotus.card.Card;
import com.kartago.blacklotus.magicSet.MagicSet;
import com.kartago.blacklotus.printing.enums.FinishEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Printing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private UUID scryfallId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @OneToMany(mappedBy = "printing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrintingFace> printingFaces = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id", nullable = false)
    private MagicSet set;
    private String collectorNumber;
    private LocalDate releasedAt;

    private Set<FinishEnum> finishes;

    @Embedded
    private Prices prices;

    @Column(length = 512)
    private String scryfallUri; // URL hacia la impresion en Scryfall
}
