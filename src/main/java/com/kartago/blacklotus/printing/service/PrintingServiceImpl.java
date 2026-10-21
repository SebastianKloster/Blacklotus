package com.kartago.blacklotus.printing.service;

import com.kartago.blacklotus.card.model.Card;
import com.kartago.blacklotus.printing.dto.PrintingDTO;
import com.kartago.blacklotus.printing.dto.PrintingFaceDTO;
import com.kartago.blacklotus.printing.model.PrintingFace;
import com.kartago.blacklotus.exceptions.ElementoNoEncontradoException;
import com.kartago.blacklotus.printing.model.Printing;
import com.kartago.blacklotus.printing.repository.PrintingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PrintingServiceImpl implements PrintingService {
    @Autowired
    PrintingRepository repo;

    @Override
    public Printing getPrintOfCard(Long cardId) {
        return repo.findRepresentatives(List.of(cardId)).stream().findFirst()
                .orElseThrow(() -> new ElementoNoEncontradoException("No se encuentra una impresión con imágenes para la carta con id: " + cardId));
    }

    @Override
    public List<Printing> getAllPrintingOfCard(Long cardId) {
        return repo.findAllByCardId(cardId);
    }

    @Override
    public List<PrintingDTO> getPrintOfCardList(List<Card> cardList) {
        if (cardList.isEmpty()) {
            return List.of();
        }
        // Fetch one printing per card in a single query, preserving the requested card order.
        var printings = repo.findRepresentatives(cardList.stream().map(Card::getId).toList())
                .stream().collect(Collectors.toMap(p -> p.getCard().getId(), Function.identity()));
        return cardList.stream().map(card -> printings.get(card.getId()))
                .filter(Objects::nonNull).map(this::toPrintingDTO).toList();
    }

    private PrintingDTO toPrintingDTO(Printing printing) {
        Card card = printing.getCard();
        return new PrintingDTO(printing.getId(), card.getId(), card.getName(),
                card.getLayout().getValue(), printing.getPrintingFaces().stream()
                .sorted(Comparator.comparingInt(PrintingFace::getFaceIndex))
                .map(face -> new PrintingFaceDTO(face.getFaceIndex(), face.getImageUris())).toList());
    }
}
