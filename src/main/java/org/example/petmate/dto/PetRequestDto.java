package org.example.petmate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PetRequestDto {
    @NotBlank
    @Size(max = 31, min = 5)
    private String name;
}
