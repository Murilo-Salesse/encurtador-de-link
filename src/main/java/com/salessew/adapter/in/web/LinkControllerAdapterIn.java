package com.salessew.adapter.in.web;

import com.salessew.adapter.in.web.dto.ShortenLinkRequestDTO;
import com.salessew.adapter.in.web.dto.ShortenLinkResponseDTO;
import com.salessew.core.port.in.RedirectPortIn;
import com.salessew.core.port.in.ShortenLinkPortIn;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
public class LinkControllerAdapterIn {

    private final ShortenLinkPortIn shortenLinkPortIn;
    private final RedirectPortIn redirectPortIn;

    public LinkControllerAdapterIn(ShortenLinkPortIn shortenLinkPortIn, RedirectPortIn redirectPortIn) {
        this.shortenLinkPortIn = shortenLinkPortIn;
        this.redirectPortIn = redirectPortIn;
    }

    @PostMapping(path = "/links")
    public ResponseEntity<ShortenLinkResponseDTO> shortenLink(@RequestBody @Valid ShortenLinkRequestDTO req,
                                                              JwtAuthenticationToken token) {

        var userId = UUID.fromString(token.getToken().getSubject());

        var response = shortenLinkPortIn.execute(req.toDomain(userId));
        return ResponseEntity.created(URI
                .create(response.shortenLinkUrl()))
                .body(response);
    }

    @GetMapping(path = "/{linkId}")
    public ResponseEntity<ShortenLinkResponseDTO> redirect(@PathVariable(name = "linkId") String linkId,
                                                              JwtAuthenticationToken token) {


        var fullUrl = redirectPortIn.execute(linkId);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(fullUrl));

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }
}
