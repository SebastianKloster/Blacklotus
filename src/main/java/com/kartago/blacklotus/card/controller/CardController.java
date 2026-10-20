package com.kartago.blacklotus.card.controller;

import com.kartago.blacklotus.card.dto.CardDTO;
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
    @GetMapping("/id/{id}")
    public ResponseEntity<CardDTO> getById(@PathVariable Long id) {
        CardDTO cardDTO = service.toCardDTO(
                service.findById(id).orElseThrow(
                        ()-> new ElementoNoEncontradoException("No se encuentra la carta con id:"+id)
                )
        );
        return ResponseEntity.ok(cardDTO);
    }

    //Get card by name
    @GetMapping("/{name}")
    public ResponseEntity<CardDTO> getByName(@PathVariable String name) {
        CardDTO CardDTO = service.toCardDTO(
                service.findByName(name).orElseThrow(
                        ()-> new ElementoNoEncontradoException("No se encuentra la carta con el nombre: "+name)
                )
        );
        return ResponseEntity.ok(CardDTO);
    }

    //Get simplified cards by name showing while writing
    @GetMapping("/autocomplete")
    public ResponseEntity<List<CardSimplifiedDTO>> autocomplete(@RequestParam String name) {
        return ResponseEntity.ok(service.getByAutocompletedName(name));
    }

    //Get page of 60 cards for testing
    @GetMapping()
    public ResponseEntity<Page<CardDTO>> getAll(@PageableDefault(size = 60) Pageable pageable) {
        Page<CardDTO> cards = service.getAll(pageable).map(card -> service.toCardDTO(card));
        return ResponseEntity.ok(cards);
    }

    //Get sample of one random card for each layout type for testing
    @GetMapping("/layouts")
    public ResponseEntity<List<CardDTO>> getAllLayouts() {
        return ResponseEntity.ok(service.getAllLayouts().stream().map(card -> service.toCardDTO(card)).toList());
    }
}
