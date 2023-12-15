package ru.taskManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.taskManagement.config.jwt.JwtUtils;
import ru.taskManagement.dto.JwtResponseDto;
import ru.taskManagement.dto.LoginDto;
import ru.taskManagement.dto.MessageResponseDto;
import ru.taskManagement.dto.UserDto;
import ru.taskManagement.enumeration.ERole;
import ru.taskManagement.model.Role;
import ru.taskManagement.model.User;
import ru.taskManagement.provider.GetProvider;
import ru.taskManagement.repository.UserRepository;
import ru.taskManagement.service.UserDetailsImpl;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Авторизация и аутентификация",
        description = "Контроллер отвечающий за авторизацию и аутентификацию пользователей")
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GetProvider provider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "Авторизация",
            description = "Выдача токена пользователю")
    @PostMapping("/signin")
    public ResponseEntity<JwtResponseDto> authUser(@Parameter(description = "Email и пароль") @RequestBody @Valid LoginDto loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponseDto(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @Operation(summary = "Регистрация",
            description = "Создание нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Email или имя пользователя уже существуют")
    })
    @PostMapping("/signup")
    public ResponseEntity<MessageResponseDto> registerUser(@RequestBody @Valid UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDto("Ошибка: Email уже существует"));
        }
        if (userRepository.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDto("Ошибка: имя пользователя уже существует"));
        }

        saveUser(userDto);

        return new ResponseEntity<>(new MessageResponseDto("Пользователь создан"), HttpStatus.CREATED);
    }

    private void saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Set<String> reqRoles = userDto.getRoles();
        Set<Role> roles = new HashSet<>();

        if (reqRoles == null) {
            Role userRole = provider.getRileByName(ERole.ROLE_USER);
            roles.add(userRole);
        } else {
            reqRoles.forEach(r -> {
                switch (r) {
                    default:
                        Role userRole = provider.getRileByName(ERole.ROLE_USER);
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
    }
}
