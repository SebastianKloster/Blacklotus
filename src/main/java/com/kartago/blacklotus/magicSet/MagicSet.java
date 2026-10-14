package com.kartago.blacklotus.magicSet;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.UUID;

public class MagicSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private UUID scryfallId;

    @Column(unique = true)
    private String code;
    private String name;
    private LocalDate releasedAt;
}
