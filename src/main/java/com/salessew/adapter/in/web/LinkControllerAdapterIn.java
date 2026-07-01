package com.salessew.adapter.in.web;

import com.salessew.adapter.in.web.dto.*;
import com.salessew.core.domain.LinkFilter;
import com.salessew.core.port.in.LinkAnalyticsPortIn;
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
import java.time.LocalDate;
import java.util.UUID;

@RestController
public class LinkControllerAdapterIn {

    private final ShortenLinkPortIn shortenLinkPortIn;
    private final RedirectPortIn redirectPortIn;
    private final MyLinksPortIn myLinksPortIn;
    private final LinkAnalyticsPortIn linkAnalyticsPortIn;

    public LinkControllerAdapterIn(ShortenLinkPortIn shortenLinkPortIn, RedirectPortIn redirectPortIn,
                                   MyLinksPortIn myLinksPortIn, LinkAnalyticsPortIn linkAnalyticsPortIn) {
        this.shortenLinkPortIn = shortenLinkPortIn;
        this.redirectPortIn = redirectPortIn;
        this.myLinksPortIn = myLinksPortIn;
        this.linkAnalyticsPortIn = linkAnalyticsPortIn;
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
                                                                     @RequestParam(name = "active", required = false) Boolean active,
                                                                     @RequestParam(name = "startCreatedAt", required = false) LocalDate startCreatedAt,
                                                                     @RequestParam(name = "endCreatedAt", required = false) LocalDate endCreatedAt,
                                                                     JwtAuthenticationToken token) {

        var userId = String.valueOf(token.getTokenAttributes().get("sub"));
        var body = myLinksPortIn.execute(userId, nextToken, limit, new LinkFilter(active, startCreatedAt, endCreatedAt));

        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        body.items()
                                .stream()
                                .map(LinkResponseDTO::fromDomain).toList(),
                        body.nextToken()
                )
        );
    }

    @GetMapping(path = "/links/{linkId}/analytics")
    public ResponseEntity<AnalyticsResponseDTO> linkAnalytics(@PathVariable String linkId,
                                                              @RequestParam(name = "startCreatedAt") LocalDate startDate,
                                                              @RequestParam(name = "endCreatedAt") LocalDate endDate,
                                                              JwtAuthenticationToken token) {

        var userId = String.valueOf(token.getTokenAttributes().get("sub"));
        var body = linkAnalyticsPortIn.execute(userId, linkId, startDate, endDate);

        return ResponseEntity.ok(body);
    }
}
