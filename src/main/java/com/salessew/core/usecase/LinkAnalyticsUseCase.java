package com.salessew.core.usecase;

import com.salessew.adapter.in.web.dto.AnalyticsDayResponseDTO;
import com.salessew.adapter.in.web.dto.AnalyticsResponseDTO;
import com.salessew.core.domain.LinkAnalytics;
import com.salessew.core.exception.FilterException;
import com.salessew.core.exception.LinkNotAllowedException;
import com.salessew.core.exception.LinkNotFoundException;
import com.salessew.core.port.in.LinkAnalyticsPortIn;
import com.salessew.core.port.out.AnalyticsRepositoryPortOut;
import com.salessew.core.port.out.LinkRepositoryPortOut;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class LinkAnalyticsUseCase implements LinkAnalyticsPortIn {

    private final LinkRepositoryPortOut linkRepositoryPortOut;
    private final AnalyticsRepositoryPortOut analyticsRepositoryPortOut;

    public LinkAnalyticsUseCase(LinkRepositoryPortOut linkRepositoryPortOut, AnalyticsRepositoryPortOut analyticsRepositoryPortOut) {
        this.linkRepositoryPortOut = linkRepositoryPortOut;
        this.analyticsRepositoryPortOut = analyticsRepositoryPortOut;
    }

    @Override
    public AnalyticsResponseDTO execute(String userId, String linkId, LocalDate startDate, LocalDate endDate) {

        validateRange(startDate, endDate);

        var link = linkRepositoryPortOut.findById(linkId).
                orElseThrow(LinkNotFoundException::new);

        if (!link.isUserOwner(UUID.fromString(userId))){
            throw new LinkNotAllowedException();
        }

        var linkAnalytics = analyticsRepositoryPortOut.findAll(linkId, startDate, endDate);

        var totalVisitors = getTotalVisitors(linkAnalytics);
        var analyticsPerDay = getAnalyticsPerDay(linkAnalytics);

        return new AnalyticsResponseDTO(totalVisitors, analyticsPerDay);
    }

    public void validateRange(LocalDate startDate, LocalDate endDate){
        if (startDate.isAfter(endDate)) {
            throw new FilterException(Map.of("startDate", "must be before endDate"));
        }
    }

    private List<AnalyticsDayResponseDTO> getAnalyticsPerDay(List<LinkAnalytics> linkAnalytics) {

        return linkAnalytics.stream()
                .map(AnalyticsDayResponseDTO::fromDomain)
                .toList();
    }

    private Long getTotalVisitors(List<LinkAnalytics> linkAnalytics) {
        return linkAnalytics.stream()
                .map(LinkAnalytics::getClicks)
                .reduce(0L, Long::sum);
    }
}
