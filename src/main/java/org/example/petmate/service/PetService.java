package org.example.petmate.service;

import lombok.AllArgsConstructor;
import org.example.petmate.dto.PetRequestDto;
import org.example.petmate.models.Pet;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class PetService {

    private final UserService userService;

    private Long idCounter;
    private final Map<Long, Pet> petMap;

    public PetService(UserService userService) {
        this.userService = userService;
        this.idCounter = 0L;
        this.petMap = new HashMap<>();
    }


    public Pet createPet(Long userId, PetRequestDto petRequestDto) {

        if(!userService.getUserMap().containsKey(userId)) {
            throw new NoSuchElementException("User is not exist");
        }

        if (
                petMap.values()
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
}
