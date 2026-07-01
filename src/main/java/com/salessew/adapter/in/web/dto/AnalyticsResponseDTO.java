package com.salessew.adapter.in.web.dto;

import java.util.List;

public record AnalyticsResponseDTO(Long totalVisitors,
                                   List<AnalyticsDayResponseDTO> analytics) {
}
