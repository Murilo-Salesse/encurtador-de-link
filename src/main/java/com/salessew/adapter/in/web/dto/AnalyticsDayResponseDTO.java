package com.salessew.adapter.in.web.dto;

import com.salessew.core.domain.LinkAnalytics;

import java.time.LocalDate;

public record AnalyticsDayResponseDTO(LocalDate date,
                                      Long totalVisitors) {


    public static AnalyticsDayResponseDTO fromDomain(LinkAnalytics linkAnalytics) {

        return new AnalyticsDayResponseDTO(linkAnalytics.getDate(), linkAnalytics.getClicks());
    }
}
