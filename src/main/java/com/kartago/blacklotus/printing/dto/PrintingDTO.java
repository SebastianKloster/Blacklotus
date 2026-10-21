package com.kartago.blacklotus.printing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrintingDTO {
    private Long id;

    //Card core-data
    private Long cardId;
    private String name;
    private String layout;

    private List<PrintingFaceDTO> printingFaces;



}
