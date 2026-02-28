package org.example.petmate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Tag(name = "PetController", description = "Операции с питомцами")
public class PetController {

    private final PetService petService;

    private final Logger log = LoggerFactory.getLogger(PetController.class);

    @PostMapping("/{userId}/pets")
    @Operation(
            summary = "Создание питомца",
            description = "Создат нового питомца добавляя его к пользователю"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Питомец успешно создан!"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации данных"
            )
    })
    public ResponseEntity<Pet> createPet(
            @PathVariable Long userId,
            @Valid @RequestBody PetRequestDto petRequestDto
    ) {
        log.info("Got request for create pet: {}", petRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(petService.createPet(userId, petRequestDto));
    }

    @GetMapping("/{petId}/pets")
    @Operation(
            summary = "Получение питомца",
            description = "Находит питомца по Id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Питомец успешно найден!"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Питомец не был найден"
            )
    })
    public ResponseEntity<Pet> findPetById(@PathVariable Long petId) {

        log.info("Got request for find pet by id: {}", petId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(petService.findPetById(petId));
    }


    @PutMapping("/{petId}/pets")
    @Operation(
            summary = "Обновление питомца",
            description = "Находит питомца по Id и обновляет его поля"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Питомец успешно изменен!"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Питомец не был найден"
            )
    })
    public ResponseEntity<Pet> updatePetById(
            @PathVariable Long petId,
            @Valid @RequestBody PetRequestDto petRequest
    ) {

        log.info("Got request for update pet with id: {}", petId);
        return ResponseEntity
                .status(200)
                .body(petService.updatePet(petId, petRequest));
    }

    @DeleteMapping("/{petId}/pets")
    @Operation(
            summary = "Удаление питомца",
            description = "Находит питомца по Id и удаляет его"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Питомец успешно анигилирован!"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Питомец не был найден"
            )
    })
    public ResponseEntity<Void> deletePetById(@PathVariable Long petId) {
        log.info("Got request for delete pet with id: {}", petId);

        petService.deletePet(petId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/pets")
    @Operation(
            summary = "Получение всех питомцев",
            description = "Просто выводит весь список со всеми питомцами"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Все питомцы успешно найдены!"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Питомецы не были найдены"
            )
    })
    public ResponseEntity<List<Pet>> findAllPets() {
        return ResponseEntity
                .status(200)
                .body(petService.findAllPets());
    }
}
