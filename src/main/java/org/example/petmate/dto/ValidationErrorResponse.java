package org.example.petmate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@Schema(description = "Ответ с ошибками валидации")
public class ValidationErrorResponse {
    @Schema(description = "Общее сообщение об ошибке")
    private String message;
    @Schema(description = "Ошибки по полям")
    private List<FieldError> fieldErrors;

    @Data
    @AllArgsConstructor
    @Schema(description = "Ошибка конкретного поля")
    public static class FieldError{
        private String field;
        private String message;
        private Object rejectedValue;
    }
}
