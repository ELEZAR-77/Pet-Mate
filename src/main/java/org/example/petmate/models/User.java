package org.example.petmate.models;

import lombok.AllArgsConstructor;
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
    private List<Pet> pets;


    public void addPet(Pet pet) {
        pets.add(pet);
    }

    public User(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.pets = new ArrayList<>();
    }
}
