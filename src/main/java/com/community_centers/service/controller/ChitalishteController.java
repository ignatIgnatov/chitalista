package com.community_centers.service.controller;

import com.community_centers.service.entity.Chitalishte;
import com.community_centers.service.entity.InformationCard;
import com.community_centers.service.service.ChitalishteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chitalishta")
public class ChitalishteController {

    private final ChitalishteService chitalishteService;

    // Chitalishte CRUD endpoints

    @GetMapping
    public ResponseEntity<Page<Chitalishte>> getAllChitalista(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Chitalishte> response = chitalishteService.getAllChitalista(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Chitalishte>> getAllChitalista() {
        List<Chitalishte> response = chitalishteService.getAllChitalista();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chitalishte> getChitalishteById(@PathVariable Integer id) {
        Chitalishte response = chitalishteService.getChitalishteById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Chitalishte> createChitalishte(@RequestBody Chitalishte chitalishte) {
        Chitalishte created = chitalishteService.createChitalishte(chitalishte);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chitalishte> updateChitalishte(
            @PathVariable Integer id,
            @RequestBody Chitalishte chitalishteDetails) {
        try {
            Chitalishte updatedChitalishte = chitalishteService.updateChitalishte(id, chitalishteDetails);
            return ResponseEntity.ok(updatedChitalishte);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChitalishte(@PathVariable Integer id) {
        try {
            chitalishteService.deleteChitalishte(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // InformationCard CRUD endpoints

    @GetMapping("/{id}/information-cards")
    public ResponseEntity<List<InformationCard>> getInformationCardsByChitalishte(@PathVariable Integer id) {
        List<InformationCard> response = chitalishteService.getInformationCardsByChitalishteId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/information-cards/{cardId}")
    public ResponseEntity<InformationCard> getInformationCardById(@PathVariable Integer cardId) {
        InformationCard response = chitalishteService.getInformationCardById(cardId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/information-cards")
    public ResponseEntity<InformationCard> createInformationCard(
            @PathVariable Integer id,
            @RequestBody InformationCard informationCard) {
        try {
            InformationCard createdCard = chitalishteService.createInformationCard(id, informationCard);
            return ResponseEntity.ok(createdCard);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/information-cards/{cardId}")
    public ResponseEntity<InformationCard> updateInformationCard(
            @PathVariable Integer cardId,
            @RequestBody InformationCard cardDetails) {
        try {
            InformationCard updatedCard = chitalishteService.updateInformationCard(cardId, cardDetails);
            return ResponseEntity.ok(updatedCard);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/information-cards/{cardId}")
    public ResponseEntity<Void> deleteInformationCard(@PathVariable Integer cardId) {
        try {
            chitalishteService.deleteInformationCard(cardId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Search and additional endpoints

    @GetMapping("/search")
    public ResponseEntity<Page<Chitalishte>> searchChitalishta(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String municipality,
            @RequestParam(required = false) String town,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Chitalishte> response = chitalishteService.searchChitalishtaAdvanced(
                name, region, municipality, town, pageable
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCount() {
        Long count = chitalishteService.getTotalCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/information-cards/all")
    public ResponseEntity<List<InformationCard>> getAllInformationCards() {
        List<InformationCard> response = chitalishteService.getAllInformationCards();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> chitalishteExists(@PathVariable Integer id) {
        boolean exists = chitalishteService.chitalishteExists(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/information-cards/{cardId}/exists")
    public ResponseEntity<Boolean> informationCardExists(@PathVariable Integer cardId) {
        boolean exists = chitalishteService.informationCardExists(cardId);
        return ResponseEntity.ok(exists);
    }
}