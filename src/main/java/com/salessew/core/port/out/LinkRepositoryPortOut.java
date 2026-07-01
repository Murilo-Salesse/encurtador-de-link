package com.salessew.core.port.out;

import com.salessew.core.domain.Link;
import com.salessew.core.domain.LinkFilter;
import com.salessew.core.domain.PaginatedResult;

import java.util.Optional;

public interface LinkRepositoryPortOut {

    Link save(Link link);
    Optional<Link> findById(String linkId);
    PaginatedResult<Link> findAllByUserId(String userId,
                                          String nextToken,
                                          int limit,
                                          LinkFilter filters);
}
