package org.example.petmate.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.petmate.dto.UserRequestDto;
import org.example.petmate.models.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private Long idCounter;

    @Getter
    private Map<Long, User> userMap;

    public UserService() {
        this.idCounter = 0L;
        this.userMap = new HashMap<>();
    }

    public User createUser(UserRequestDto request) {
        if (
                userMap.values()
                .stream()
                .anyMatch(
                        u -> u.getEmail().equals(request.getEmail())
                )
        ) {
            throw  new IllegalArgumentException("Email already exist");
        }

        Long newId = ++idCounter;

        User user = new User();
        user.setId(newId);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());

        userMap.put(newId, user);

        return user;
    }

    public User findUserById(Long id) {
        return Optional.ofNullable(userMap.get(id))
                .orElseThrow(() -> new NoSuchElementException("user with id " + id + " is not exist"));
    }

    public User updateUser(
            Long id,
            UserRequestDto request
    ) {
        if (!userMap.containsKey(id)) {
            throw new NoSuchElementException("user with id " + id + " is not exist");
        }

        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setName(request.getName());
        updateUser.setEmail(request.getEmail());
        updateUser.setAge(request.getAge());

        userMap.put(id, updateUser);
        
        return updateUser;
    }

    public void deleteUserById(Long id) {
        if (!userMap.containsKey(id)) {
            throw new NoSuchElementException("user with id " + id + " is not exist");
        }

        userMap.remove(id);
    }
}
