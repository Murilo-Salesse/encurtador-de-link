package com.salessew.core.usecase;

import com.salessew.adapter.in.web.dto.ShortenLinkResponseDTO;
import com.salessew.core.domain.Link;
import com.salessew.core.exception.LinkAlreadyExistException;
import com.salessew.core.port.in.ShortenLinkPortIn;
import com.salessew.core.port.out.LinkRepositoryPortOut;
import org.springframework.stereotype.Component;

@Component
public class ShortenLinkUseCase implements ShortenLinkPortIn {

    private final LinkRepositoryPortOut linkRepositoryPortOut;

    public ShortenLinkUseCase(LinkRepositoryPortOut linkRepositoryPortOut) {
        this.linkRepositoryPortOut = linkRepositoryPortOut;
    }

    @Override
    public ShortenLinkResponseDTO execute(Link link) {

       var linkOpt = linkRepositoryPortOut.findById(link.getLinkId());

       if (linkOpt.isPresent()) {
           throw new LinkAlreadyExistException();
       }

        linkRepositoryPortOut.save(link);
        return new ShortenLinkResponseDTO("http://localhost:3000/" + link.getLinkId());
    }
}
