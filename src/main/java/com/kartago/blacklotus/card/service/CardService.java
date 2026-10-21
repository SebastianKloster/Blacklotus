package com.kartago.blacklotus.card.service;

import com.kartago.blacklotus.card.dto.CardDTO;
import com.kartago.blacklotus.card.dto.CardSimplifiedDTO;
import com.kartago.blacklotus.card.model.Card;
import com.kartago.blacklotus.printing.dto.PrintingDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


public interface CardService {
    Optional<Card> findById(Long id);
    Optional<Card> findByName(String name);

    List<CardSimplifiedDTO> getByAutocompletedName(String name);
    Page<PrintingDTO> getAll(Pageable pageable);
    List<PrintingDTO> getAllLayouts();

    CardDTO toCardDTO(Card card);
    CardSimplifiedDTO toCardSimplifiedDTO(Card card);
}
