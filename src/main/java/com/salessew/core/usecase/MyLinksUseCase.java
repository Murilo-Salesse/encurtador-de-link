package com.salessew.core.usecase;

import com.salessew.core.domain.Link;
import com.salessew.core.domain.LinkFilter;
import com.salessew.core.domain.PaginatedResult;
import com.salessew.core.port.in.MyLinksPortIn;
import com.salessew.core.port.out.LinkRepositoryPortOut;
import org.springframework.stereotype.Component;

@Component
public class MyLinksUseCase implements MyLinksPortIn {

    private final LinkRepositoryPortOut linkRepositoryPortOut;

    public MyLinksUseCase(LinkRepositoryPortOut linkRepositoryPortOut) {
        this.linkRepositoryPortOut = linkRepositoryPortOut;
    }

    @Override
    public PaginatedResult<Link> execute(String userId,
                                         String nextToken,
                                         int limit,
                                         LinkFilter filters) {

        filters.validate();

        return linkRepositoryPortOut.findAllByUserId(userId,
                nextToken,
                limit,
                filters);
    }
}
