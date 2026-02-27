package org.example.petmate.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private List<Pet> pets = new ArrayList<>();


    public void addPet(Pet pet) {
        pets.add(pet);
    }
}
