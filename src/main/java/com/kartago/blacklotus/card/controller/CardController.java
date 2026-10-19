package com.kartago.blacklotus.card.controller;

import com.kartago.blacklotus.card.dto.CardSimplifiedDTO;
import com.kartago.blacklotus.card.model.Card;
import com.kartago.blacklotus.card.service.CardService;
import com.kartago.blacklotus.exceptions.ElementoNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("api/card")
public class CardController {
    @Autowired
    CardService service;

    //Get card by id
    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable Long id) {
        Card card = service.findById(id).orElseThrow(
                ()-> new ElementoNoEncontradoException("No se encuentra la carta con id:"+id)
        );
        return ResponseEntity.ok(card);
    }

    //Get card by name
    @GetMapping("/{name}")
    public ResponseEntity<Card> getByName(@PathVariable String name) {
        Card card = service.findByName(name).orElseThrow(
                ()-> new ElementoNoEncontradoException("No se encuentra la carta con el nombre: "+name)
        );
        return ResponseEntity.ok(card);
    }

    //Get simplified cards by name showing while writing
    @GetMapping("/autocomplete")
    public ResponseEntity<List<CardSimplifiedDTO>> autocomplete(@RequestParam String name) {
        return ResponseEntity.ok(service.getByAutocompletedName(name));
    }

    //Get page of 60 cards for testing
    @GetMapping()
    public ResponseEntity<Page<Card>> getAll(@PageableDefault(size = 60) Pageable pageable) {
        Page<Card> cards = service.getAll(pageable);
        return ResponseEntity.ok(cards);
    }

    //Get sample of one random card for each layout type for testing

}
