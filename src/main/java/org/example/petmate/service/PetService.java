package org.example.petmate.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.petmate.dto.PetRequestDto;
import org.example.petmate.models.Pet;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final UserService userService;

    private Long idCounter;

    public PetService(UserService userService) {
        this.userService = userService;
        this.idCounter = 0L;
    }


    public Pet createPet(Long userId, PetRequestDto petRequestDto) {

        if(!userService.getUserMap().containsKey(userId)) {
            throw new NoSuchElementException("User is not exist");
        }

        if (
                petMap().values()
                        .stream()
                        .anyMatch(
                                pet -> pet.getName().equals(petRequestDto.getName())
                        )
        ) {
            throw new IllegalArgumentException("Name already exist");
        }

        Long newId = ++idCounter;
        var pet = new Pet();
        pet.setId(newId);
        pet.setName(petRequestDto.getName());
        pet.setUserId(userId);

        var user = userService.getUserMap().get(userId);
        user.addPet(pet);
        userService.getUserMap().put(userId, user);

        return pet;
    }

    public Pet findPetById(Long id) {
        if(!petMap().containsKey(id)) {
            throw new NoSuchElementException("Pet with id " + id + " is not exist");
        }

        return petMap().get(id);
    }

    public Map<Long, Pet> petMap() {
        return userService.getUserMap().values()
                .stream().flatMap(u -> u.getPets().stream())
                .collect(Collectors.toMap(
                        Pet::getId,
                        pet -> pet
                ));
    }

    public Pet updatePet(
            Long petId,
            PetRequestDto petRequest
    ) {
        if (!petMap().containsKey(petId)) {
            throw new NoSuchElementException("Pet with id " + petId + " is not exist");
        }

        Pet updatedPet = petMap().get(petId);
        updatedPet.setName(petRequest.getName());

        petMap().put(petId, updatedPet);

        return updatedPet;
    }

    public void deletePet(Long petId) {
        if (!petMap().containsKey(petId)) {
            throw new NoSuchElementException("Pet with id " + petId + " is not exist");
        }

        var owner = userService.getUserMap().values()
                .stream()
                .filter(
                        user -> user.getPets().stream()
                                .anyMatch(pet -> pet.getId().equals(petId))
                ).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Pet with id " + petId + " is not exist"));

        owner.getPets().removeIf(pet -> pet.getId().equals(petId));
    }

    public List<Pet> findAllPets() {
        if (petMap().values().stream().toList().isEmpty()) {
            throw new NoSuchElementException("Список пуст!");
        }
        return petMap().values().stream().toList();
    }
}
