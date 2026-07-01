package com.salessew.core.port.out;

import com.salessew.core.domain.Link;
import com.salessew.core.domain.LinkAnalytics;

import java.time.LocalDate;
import java.util.List;

public interface AnalyticsRepositoryPortOut {

    void updateClickCount(Link link);
    List<LinkAnalytics> findAll(String linkId,
                                LocalDate startDate,
                                LocalDate endDate);
}
