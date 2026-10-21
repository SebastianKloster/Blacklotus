package com.kartago.blacklotus.printing.service;

import com.kartago.blacklotus.card.model.Card;
import com.kartago.blacklotus.printing.dto.PrintingDTO;
import com.kartago.blacklotus.printing.model.Printing;

import java.util.List;

public interface PrintingService {
//    Printing getPrintOfCard(Card card);
    Printing getPrintOfCard(Long cardId);

//    List<Printing> getAllPrintingOfCard(Card card);
    List<Printing> getAllPrintingOfCard(Long cardId);

    List<PrintingDTO> getPrintOfCardList(List<Card> cardList);
}
