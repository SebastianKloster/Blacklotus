package com.kartago.blacklotus.expansion.service;

import com.kartago.blacklotus.expansion.model.Expansion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ExpansionService {
    Optional<Expansion> findById(Long id);
    Optional<Expansion> findByName(String name);
    Optional<Expansion> findByCode(String code);

    Page<Expansion> getAll(Pageable pageable);

}
