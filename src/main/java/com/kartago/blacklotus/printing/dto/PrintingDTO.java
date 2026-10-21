package com.kartago.blacklotus.printing.dto;

import com.kartago.blacklotus.card.dto.CardDTO;
import com.kartago.blacklotus.printing.model.enums.FinishEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrintingDTO {
    private Long id;

    //Card core-data
    private Long cardId;
    private String name;
    private boolean isGameChanger;

    private List<PrintingFaceDTO> printingFaces;

    private Set<FinishEnum> finishes; //Finishes disponibles del print
    private String scryfallUri; // URL hacia la impresion en Scryfall


}
