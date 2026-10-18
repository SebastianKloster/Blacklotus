package com.kartago.blacklotus.printing.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class ImageUris {
    @Column(length = 512)
    private String thumb;
    @Column(length = 512)
    private String grid;
    @Column(length = 512)
    private String display;
    @Column(length = 512)
    private String crop;
    @Column(length = 512)
    private String art;
}
