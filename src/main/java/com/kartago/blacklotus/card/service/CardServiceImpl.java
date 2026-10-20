package com.kartago.blacklotus.card.service;

import com.kartago.blacklotus.card.dto.CardDTO;
import com.kartago.blacklotus.card.dto.CardSimplifiedDTO;
import com.kartago.blacklotus.card.model.Card;
import com.kartago.blacklotus.card.repository.CardRepository;
import com.kartago.blacklotus.printing.model.Printing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService{
    @Autowired
    CardRepository repo;

    @Override
    public Optional<Card> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Card> findByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public List<CardSimplifiedDTO> getByAutocompletedName(String name) {
        List<Card> cardList = repo.findTop5ByNameStartingWithIgnoreCase(name);
        return cardList.stream().map(this::toCardSimplifiedDTO).toList();
    }

    @Override
    public Page<Card> getAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public List<Card> getAllLayouts() {
        return repo.findRandomCardPerLayout();
    }

    @Override
    public CardDTO toCardDTO(Card card) {
        List<CardDTO.CardFaceDTO> cardFaces = card.getCardFaces().stream()
                .map(face -> new CardDTO.CardFaceDTO(
                        face.getId(),
                        face.getFaceIndex(),
                        face.getName(),
                        face.getManaCost(),
                        face.getManaValue(),
                        face.getColors(),
                        face.getTypeLine(),
                        face.getOracleText(),
                        face.getPower(),
                        face.getToughness(),
                        face.getLoyalty(),
                        face.getDefense()
                ))
                .toList();

        return new CardDTO(
                card.getId(),
                card.getOracleId(),
                cardFaces,
                card.getLayout(),
                card.getPrintings().stream().map(Printing::getId).toList(),
                card.getName(),
                card.getManaValue(),
                card.getColorIdentity(),
                card.isGameChanger(),
                card.getLegalities()
        );
    }

    @Override
    public CardSimplifiedDTO toCardSimplifiedDTO(Card card) {
        return new CardSimplifiedDTO(
                card.getId(), //id
                card.getName() //name
        );
    }

}
