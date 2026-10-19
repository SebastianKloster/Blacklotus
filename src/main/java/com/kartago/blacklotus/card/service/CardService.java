package com.kartago.blacklotus.card.service;

import com.kartago.blacklotus.card.dto.CardSimplifiedDTO;
import com.kartago.blacklotus.card.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface CardService {
    Optional<Card> findById(Long id);
    Optional<Card> findByName(String name);

    List<CardSimplifiedDTO> getByAutocompletedName(String name);
    Page<Card> getAll(Pageable pageable);
    List<Card> getAllLayouts();
}
