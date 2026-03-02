package org.example.petmate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserRequestDto {
    @NotBlank
    @Size(max = 31, min = 5)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Max(100)
    @Min(6)
    private Integer age;

    @Override
    public String toString() {
        return "RequestUser{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
