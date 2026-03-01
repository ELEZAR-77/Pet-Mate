package org.example.petmate;

import org.example.petmate.dto.PetRequestDto;
import org.example.petmate.dto.UserRequestDto;
import org.example.petmate.models.Pet;
import org.example.petmate.service.PetService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final PetRequestDto testPet = new PetRequestDto("Sobaka");

    UserRequestDto testUser = new UserRequestDto(
            "Testname",
            "testname@mail.com",
            20
    );

    @Test
    void shouldSuccessCreatePetWhenRequestValid() throws Exception {

        var user = userService.createUser(testUser);

        String petJson = objectMapper.writeValueAsString(testPet);

        String petRequestJson = mockMvc.perform(
                post("/users/{userId}/pets", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson)
        )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var petResponse = objectMapper.readValue(petRequestJson, Pet.class);

        Assertions.assertNotNull(petResponse.getId());
        Assertions.assertEquals(petResponse.getName(), testPet.getName());
        Assertions.assertNotNull(petResponse.getUserId());

        userService.deleteUserById(user.getId());
    }

    @Test
    void shouldNotSuccessCreatePetWhenRequestNotValid() throws Exception {

        testPet.setName("Cat");

        var user = userService.createUser(testUser);

        String petJson = objectMapper.writeValueAsString(testPet);

        mockMvc.perform(
                        post("/users/{userId}/pets", user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(petJson)
                )
                .andExpect(status().isBadRequest());

        userService.deleteUserById(user.getId());
    }

    @Test
    void shouldSuccessFindPetById() throws Exception {
        var user = userService.createUser(testUser);
        var pet = petService.createPet(user.getId(), testPet);


        mockMvc.perform(
                get("/users/{petId}/pets", pet.getId())
        ).andExpect(status().isOk());

        userService.deleteUserById(user.getId());
    }

    @Test
    void shouldNotSuccessWhenPetNotFound() throws Exception {
        mockMvc.perform(
                get("/users/{petId}/pets", Integer.MAX_VALUE)
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldSuccessUpdatePetWhenRequestValid() throws Exception {

        var user = userService.createUser(testUser);
        var pet = petService.createPet(user.getId(), testPet);

        testPet.setName("Bober");

        String petJson = objectMapper.writeValueAsString(testPet);

        mockMvc.perform(
                        put("/users/{petId}/pets", pet.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(petJson)
                )
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotSuccessUpdatePetWhenRequestNotValid() throws Exception {

        var user = userService.createUser(testUser);
        var pet = petService.createPet(user.getId(), testPet);

        testPet.setName("Bob");

        String petJson = objectMapper.writeValueAsString(testPet);

        mockMvc.perform(
                        put("/users/{petId}/pets", pet.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(petJson)
                )
                .andExpect(status().isBadRequest());

        userService.deleteUserById(user.getId());
    }

    @Test
    void shouldNotSuccessUpdatePetWhenPetNotFound() throws Exception {
        String petJson = objectMapper.writeValueAsString(testPet);

        mockMvc.perform(
                        put("/users/{petId}/pets", Integer.MAX_VALUE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(petJson)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSuccessDeletePetByIdWhenRequestValid() throws Exception {
        var user = userService.createUser(testUser);
        var pet = petService.createPet(user.getId(), testPet);

        mockMvc.perform(
                        delete("/users/{petId}/pets", pet.getId())
                )
                .andExpect(status().isNoContent());

        userService.deleteUserById(user.getId());

        System.out.println(userService.getUserMap());
    }

    @Test
    void shouldNotSuccessDeletePetByIdWhenPetNotFound() throws Exception {

        mockMvc.perform(
                        delete("/users/{petId}/pets", Integer.MAX_VALUE)
                )
                .andExpect(status().isNotFound());
    }
}
