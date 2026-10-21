package com.kartago.blacklotus.expansion.service;

import com.kartago.blacklotus.expansion.model.Expansion;
import com.kartago.blacklotus.expansion.repository.ExpansionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExpansionServiceImpl implements ExpansionService {
    @Autowired
    ExpansionRepository repo;

    @Override
    public Optional<Expansion> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Expansion> findByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public Optional<Expansion> findByCode(String code) {
        return repo.findByCode(code);
    }

    @Override
    public Page<Expansion> getAll(Pageable pageable) {
        return repo.findAll(pageable);
    }
}
