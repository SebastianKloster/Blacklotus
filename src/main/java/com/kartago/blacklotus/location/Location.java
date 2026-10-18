package com.kartago.blacklotus.location;

import com.kartago.blacklotus.location.enums.LocationTypeEnum;
import com.kartago.blacklotus.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private User user;
    private String name;

    @Enumerated(EnumType.STRING)
    private LocationTypeEnum locationType;
}
