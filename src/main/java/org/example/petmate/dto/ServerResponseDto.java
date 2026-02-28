package org.example.petmate.dto;

import java.time.LocalDateTime;

public record ServerResponseDto(
    String message,
    String detailedMessage,
    LocalDateTime localDateTime
){}
