package com.salessew.adapter.in.web;

import com.salessew.adapter.in.web.dto.LoginRequestDTO;
import com.salessew.adapter.in.web.dto.LoginResponseDTO;
import com.salessew.core.usecase.AuthNUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenControllerAdapterIn {

    private final AuthNUseCase authNUseCase;

    public TokenControllerAdapterIn(AuthNUseCase authNUseCase) {
        this.authNUseCase = authNUseCase;
    }

    @PostMapping(path = "/oauth/token")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        var response = authNUseCase.execute(loginRequestDTO);
        return ResponseEntity.ok(response);
    }
}
