package com.kartago.blacklotus.printing;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class ImageUris {
    @Column(length = 512)
    private String small;
    @Column(length = 512)
    private String normal;
    @Column(length = 512)
    private String large;
    @Column(length = 512)
    private String png;
    @Column(length = 512)
    private String artCrop;
    @Column(length = 512)
    private String borderCrop;
    @Column(length = 512)
    private String thumb;
    @Column(length = 512)
    private String grid;
    @Column(length = 512)
    private String display;
    @Column(length = 512)
    private String art;
    @Column(length = 512)
    private String crop;
}
