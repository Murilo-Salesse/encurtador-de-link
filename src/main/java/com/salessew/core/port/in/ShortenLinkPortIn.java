package com.salessew.core.port.in;

import com.salessew.adapter.in.web.dto.ShortenLinkResponseDTO;
import com.salessew.core.domain.Link;

public interface ShortenLinkPortIn {

    ShortenLinkResponseDTO execute(Link req);
}
