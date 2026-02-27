package org.example.petmate.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.petmate.dto.PetRequestDto;
import org.example.petmate.models.Pet;
import org.example.petmate.service.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Tag(name = "PetController", description = "Операции с питомцами")
public class PetController {

    private final PetService petService;

    private final Logger log = LoggerFactory.getLogger(PetController.class);

    @PostMapping("/{userId}/create")
    public ResponseEntity<Pet> createPet(
            @PathVariable Long userId,
            @Valid @RequestBody PetRequestDto petRequestDto
    ) {
        log.info("Got request for create pet: {}", petRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(petService.createPet(userId, petRequestDto));
    }
}
