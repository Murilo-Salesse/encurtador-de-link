package com.salessew.core.usecase;

import com.salessew.core.exception.LinkNotFoundException;
import com.salessew.core.port.in.RedirectPortIn;
import com.salessew.core.port.out.AnalyticsRepositoryPortOut;
import com.salessew.core.port.out.LinkRepositoryPortOut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RedirectUseCase implements RedirectPortIn {

    private final LinkRepositoryPortOut linkRepositoryPortOut;
    private final AnalyticsRepositoryPortOut analyticsRepositoryPortOut;

    public RedirectUseCase(LinkRepositoryPortOut linkRepositoryPortOut,
                           AnalyticsRepositoryPortOut analyticsRepositoryPortOut) {
        this.linkRepositoryPortOut = linkRepositoryPortOut;
        this.analyticsRepositoryPortOut = analyticsRepositoryPortOut;
    }

    @Override
    public String execute(String linkId) {

       var link = linkRepositoryPortOut.findById(linkId)
               .orElseThrow(LinkNotFoundException::new);

       if (!link.isActive()) {
           throw new LinkNotFoundException();
       }

       if (link.getExpirationDateTime().isBefore(LocalDateTime.now())) {
           throw new LinkNotFoundException();
       }

       analyticsRepositoryPortOut.updateClickCount(link);
       return link.generateFullUrl();
    }
}
