package com.kartago.blacklotus.printing;

import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class PrintingFace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Printing printing;
    private int faceIndex; //printing + faceIndex = unique

    @Embedded
    private ImageUris imageUris;
}
