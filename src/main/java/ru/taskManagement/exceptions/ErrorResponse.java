package ru.taskManagement.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class ErrorResponse {
    private HttpStatus status;

    private String reason;

    private String message;
}
