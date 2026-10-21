package com.kartago.blacklotus.expansion.repository;

import com.kartago.blacklotus.expansion.model.Expansion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpansionRepository extends JpaRepository<Expansion, Long> {
    Optional<Expansion> findByName(String name);
    Optional<Expansion> findByCode(String name);
}
