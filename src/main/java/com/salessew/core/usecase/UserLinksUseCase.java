package com.salessew.core.usecase;

import com.salessew.core.domain.Link;
import com.salessew.core.domain.LinkFilter;
import com.salessew.core.domain.PaginatedResult;
import com.salessew.core.port.in.UserLinksPortIn;
import com.salessew.core.port.out.LinkRepositoryPortOut;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserLinksUseCase implements UserLinksPortIn {

    private final LinkRepositoryPortOut linkRepositoryPortOut;

    public UserLinksUseCase(LinkRepositoryPortOut linkRepositoryPortOut) {
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
