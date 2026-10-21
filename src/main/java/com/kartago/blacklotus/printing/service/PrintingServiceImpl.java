package com.kartago.blacklotus.printing.service;

import com.kartago.blacklotus.card.model.Card;
import com.kartago.blacklotus.printing.dto.PrintingDTO;
import com.kartago.blacklotus.printing.model.Printing;
import com.kartago.blacklotus.printing.repository.PrintingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PrintingServiceImpl implements PrintingService {
    @Autowired
    PrintingRepository repo;

    @Override
    public Printing getPrintOfCard(Long cardId) {
        return null;
    }

    @Override
    public List<Printing> getAllPrintingOfCard(Long cardId) {
        return List.of();
    }

    @Override
    public List<PrintingDTO> getPrintOfCardList(List<Card> cardList) {
        return List.of();
    }
}
