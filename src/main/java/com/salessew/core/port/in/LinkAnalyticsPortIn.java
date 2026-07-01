package com.salessew.core.port.in;

import com.salessew.adapter.in.web.dto.AnalyticsResponseDTO;

import java.time.LocalDate;

public interface LinkAnalyticsPortIn {

    AnalyticsResponseDTO execute(String userId,
                                 String linkId,
                                 LocalDate startDate,
                                 LocalDate endDate);
}
