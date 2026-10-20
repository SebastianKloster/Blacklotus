package com.kartago.blacklotus.card.repository;

import com.kartago.blacklotus.card.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByName(String name);

    List<Card> findTop5ByNameStartingWithIgnoreCase(String name);

    // Usamos RAND() para MySQL y alias explícitos para asegurar el mapeo de Hibernate
    @Query(value = """
            SELECT *
            FROM (
                SELECT c.*, 
                       ROW_NUMBER() OVER (PARTITION BY c.layout ORDER BY RAND()) as row_num
                FROM card c
            ) ranked_cards
            WHERE row_num = 1
            """, nativeQuery = true)
    List<Card> findRandomCardPerLayout();
}
