package com.lbertalhia.security.controllers.dtos;

public record LoginResponseDto(String accessToken, Long expiresIn) {
}
