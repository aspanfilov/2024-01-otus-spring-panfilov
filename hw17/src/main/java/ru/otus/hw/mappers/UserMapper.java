package ru.otus.hw.mappers;

import ru.otus.hw.dtos.UserViewDTO;
import ru.otus.hw.models.User;

public class UserMapper {
    public static UserViewDTO toUserViewDTO(User user) {
        return UserViewDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
