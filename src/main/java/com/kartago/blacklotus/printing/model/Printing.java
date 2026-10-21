package com.kartago.blacklotus.printing.model;

import com.kartago.blacklotus.card.model.Card;
import com.kartago.blacklotus.expansion.model.Expansion;
import com.kartago.blacklotus.printing.model.embedded.Prices;
import com.kartago.blacklotus.printing.model.enums.FinishEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @OneToMany(mappedBy = "printing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrintingFace> printingFaces = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expansion_id", nullable = false)
    private Expansion expansion;
    private String collectorNumber;
    private LocalDate releasedAt;

    private Set<FinishEnum> finishes;

    @Embedded
    private Prices prices;

    @Column(length = 512)
    private String scryfallUri; // URL hacia la impresion en Scryfall
}
