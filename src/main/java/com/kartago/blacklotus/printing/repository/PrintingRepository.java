package com.kartago.blacklotus.printing.repository;

import com.kartago.blacklotus.printing.model.Printing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Collection;

public interface PrintingRepository extends JpaRepository<Printing, Long> {
    Page<Printing> getByCardId(Long cardId, Pageable pageable);

    @Query("""
            select distinct p from Printing p
            join fetch p.card
            join fetch p.printingFaces
            where p.card.id in :cardIds
              and p.id = (select min(candidate.id) from Printing candidate
                          where candidate.card = p.card)
            """)
    List<Printing> findRepresentatives(@Param("cardIds") Collection<Long> cardIds);

    @Query("select distinct p from Printing p left join fetch p.printingFaces where p.card.id = :cardId order by p.id")
    List<Printing> findAllByCardId(@Param("cardId") Long cardId);


}
