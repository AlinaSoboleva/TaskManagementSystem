package ru.taskManagement.dto.mapper;

import ru.taskManagement.dto.UserDto;
import ru.taskManagement.dto.UserResponseDto;
import ru.taskManagement.model.User;

public class UserMapper {
    public static UserResponseDto toResponseDto(User user) {
        if (user == null) return null;

        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }


    public static User toEntity(UserDto userDto) {
        if (userDto == null) return null;

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        return user;
    }
}
