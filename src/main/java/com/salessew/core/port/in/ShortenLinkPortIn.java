package com.salessew.core.port.in;

import com.salessew.core.domain.Link;

public interface ShortenLinkPortIn {

    String execute(Link req);
}
