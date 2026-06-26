package com.salessew.core.port.out;

import com.salessew.core.domain.Link;

import java.util.Optional;

public interface LinkRepositoryPortOut {

    Link save(Link link);
    Optional<Link> findById(String linkId);
}
