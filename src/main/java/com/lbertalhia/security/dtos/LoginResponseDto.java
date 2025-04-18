package com.lbertalhia.security.dtos;

public record LoginResponseDto(String accessToken, Long expiresIn) {
}
