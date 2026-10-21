package com.kartago.blacklotus.printing.repository;

import com.kartago.blacklotus.printing.model.Printing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrintingRepository extends JpaRepository<Printing, Long> {
    Page<Printing> getByCardId(Long cardId, Pageable pageable);


}
