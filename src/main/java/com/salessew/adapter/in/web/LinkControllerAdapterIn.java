package com.salessew.adapter.in.web;

import com.salessew.adapter.in.web.dto.ApiResponseDTO;
import com.salessew.adapter.in.web.dto.LinkResponseDTO;
import com.salessew.adapter.in.web.dto.ShortenLinkRequestDTO;
import com.salessew.adapter.in.web.dto.ShortenLinkResponseDTO;
import com.salessew.core.port.in.MyLinksPortIn;
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
    private final MyLinksPortIn myLinksPortIn;

    public LinkControllerAdapterIn(ShortenLinkPortIn shortenLinkPortIn, RedirectPortIn redirectPortIn,
                                   MyLinksPortIn myLinksPortIn) {
        this.shortenLinkPortIn = shortenLinkPortIn;
        this.redirectPortIn = redirectPortIn;
        this.myLinksPortIn = myLinksPortIn;
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
    public ResponseEntity<ShortenLinkResponseDTO> redirect(@PathVariable String linkId) {


        var fullUrl = redirectPortIn.execute(linkId);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(fullUrl));

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    @GetMapping(path = "/links")
    public ResponseEntity<ApiResponseDTO<LinkResponseDTO>> userLinks(@RequestParam(name = "nextToken", defaultValue = "") String nextToken,
                                                                     @RequestParam(name = "limit", defaultValue = "3") Integer limit,
                                                                     JwtAuthenticationToken token) {

        var userId = String.valueOf(token.getTokenAttributes().get("sub"));
        var body = myLinksPortIn.execute(userId, nextToken, limit);

        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        body.items().stream().map(LinkResponseDTO::fromDomain).toList(),
                        body.nextToken()
                )
        );
    }
}
