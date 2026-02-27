package org.example.petmate.dto;

import java.time.LocalDateTime;

public record NotFoundErrorDto (
    String message,
    String detailedMessage,
    LocalDateTime localDateTime
){}
