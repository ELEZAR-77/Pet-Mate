package org.example.petmate.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pet {
    private Long id;
    private String name;
    private Long userId;
}
