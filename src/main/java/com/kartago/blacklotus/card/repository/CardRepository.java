package com.kartago.blacklotus.card.repository;

import com.kartago.blacklotus.card.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByName(String name);

    List<Card> findTop5ByNombreStartingWithIgnoreCase(String nombre);
}
