package com.lbertalhia.security.controllers.dtos;

import java.util.List;

public record FeedResponseDto(List<FeedItemDto> data,
                              int page,
                              int pageSize,
                              int totalPages,
                              Long totalElements) {
}
