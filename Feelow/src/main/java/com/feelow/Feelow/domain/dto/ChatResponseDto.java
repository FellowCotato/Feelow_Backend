package com.feelow.Feelow.domain.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDto {
    private LocalDate localDate;
    private String input;
    private String response;
    private int point;
}
