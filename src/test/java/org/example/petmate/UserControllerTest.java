package org.example.petmate;


import org.example.petmate.dto.UserRequestDto;
import org.example.petmate.models.User;
import org.example.petmate.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    UserRequestDto testUser = new UserRequestDto(
            "Testname",
            "testname@mail.com",
            20
    );

    @Test
    void shouldSuccessCreateUser() throws Exception {

        String userJson = objectMapper.writeValueAsString(testUser);

        String userCreateRequest = mockMvc.perform(
                post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var userResponse = objectMapper.readValue(userCreateRequest, User.class);

        if (userResponse.getId() == null) {
            System.out.println(userResponse);
        }

        Assertions.assertNotNull(userResponse.getId());
        Assertions.assertEquals(testUser.getName(), userResponse.getName());
        Assertions.assertEquals(testUser.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(testUser.getAge(), userResponse.getAge());

        userService.deleteUserById(userResponse.getId());
    }

    @Test
    void shouldNotCreateUserWhenRequestNotValid() throws Exception {

        testUser.setName(null);
        String userJson = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(
                post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSuccessFindUserById() throws Exception {

        var createdUser = userService.createUser(testUser);

        String findBookRequest = mockMvc.perform(
                get("/users/{id}", createdUser.getId())
        )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var findBookResponse = objectMapper.readValue(findBookRequest, User.class);

        org.assertj.core.api.Assertions.assertThat(createdUser)
                .usingRecursiveComparison()
                .isEqualTo(findBookResponse);

        userService.deleteUserById(createdUser.getId());
    }

    @Test
    void shouldNotSuccessWhenUserNotFound() throws Exception {
        mockMvc.perform(get("/user/{id}", Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateUserByIdWhenRequestValid() throws Exception {

        var user = userService.createUser(testUser);

        testUser.setName("ChangeName");
        testUser.setEmail("change@mail.com");
        testUser.setAge(90);

        String  userJson = objectMapper.writeValueAsString(testUser);


        mockMvc.perform(
                put("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ChangeName"))
                .andExpect(jsonPath("$.email").value("change@mail.com"))
                .andExpect(jsonPath("$.age").value(90));
    }

    @Test
    void shouldNotUpdateUserByIdWhenRequestNotValid() throws Exception {
        var user = userService.createUser(testUser);

        testUser.setName("Chng");
        testUser.setEmail("changemail.com");
        testUser.setAge(1);

        String  userJson = objectMapper.writeValueAsString(testUser);


        mockMvc.perform(
                        put("/users/{id}", user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson)
                )
                .andExpect(status().isBadRequest());

        userService.deleteUserById(user.getId());
    }

    @Test
    void shouldNotUpdateUserByIdWhenUserNotFount() throws Exception {

        testUser.setName("ChangeName");
        testUser.setEmail("change@mail.com");
        testUser.setAge(90);

        String  userJson = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(
                        put("/users/{id}", Integer.MAX_VALUE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson)
                )
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldDeleteUserByIdWhenRequestValid() throws Exception {

        var user = userService.createUser(testUser);

        mockMvc.perform(
                delete("/users/{id}", user.getId())
        )
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDeleteUserByIdWhenRequestNotFound() throws Exception {
        mockMvc.perform(
                        delete("/users/{id}", Integer.MAX_VALUE)
                )
                .andExpect(status().isNotFound());
    }
}
