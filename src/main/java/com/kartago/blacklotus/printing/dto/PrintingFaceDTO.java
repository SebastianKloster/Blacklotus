package com.kartago.blacklotus.printing.dto;

import com.kartago.blacklotus.printing.model.embedded.ImageUris;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrintingFaceDTO {
    private int faceIndex;
    private ImageUris imageUris;
}
