package com.kartago.blacklotus.printing;

import jakarta.persistence.*;

@Entity
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
