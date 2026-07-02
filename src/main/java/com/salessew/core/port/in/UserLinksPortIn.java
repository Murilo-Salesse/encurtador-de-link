package com.salessew.core.port.in;

import com.salessew.core.domain.Link;
import com.salessew.core.domain.LinkFilter;
import com.salessew.core.domain.PaginatedResult;


public interface MyLinksPortIn {

    PaginatedResult<Link> execute(String uuid, String nextToken,
                                  int limit,
                                  LinkFilter filters);
}
