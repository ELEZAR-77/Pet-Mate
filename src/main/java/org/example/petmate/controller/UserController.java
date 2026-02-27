package org.example.petmate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.petmate.dto.UserRequestDto;
import org.example.petmate.dto.ValidationErrorResponse;
import org.example.petmate.models.User;
import org.example.petmate.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Tag(name = "UserController", description = "Операции с пользователем")
public class UserController {

    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(UserController.class);


    @PostMapping("/register")
    @Operation(
            summary = "Регистрация пользователя",
            description = "Создает нового пользователя на основе регистрацонных данных"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь успешно создан!"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации данных",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<User> createUser(
            @RequestBody
            @Valid UserRequestDto request) {
        log.info("Get request for create User: user{}", request);
        return ResponseEntity
                .status(201)
                .body(userService.createUser(request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение пользователя",
            description = "Находит пользователя по id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно найден!"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        log.info("Got request for get user with id: {}", id);
        return ResponseEntity
                .status(200)
                .body(userService.findUserById(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление пользователя",
            description = "Обновляет пользователя по id на основе измененных полей"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно обновлен!"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto request
    ) {
        log.info("Got request for update user with id: {}", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление пользователя",
            description = "Удаляет пользователя по id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Пользователь успешно удален!"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        log.info("Got request for delete user with id: {}", id);

        userService.deleteUserById(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();

    }
}
