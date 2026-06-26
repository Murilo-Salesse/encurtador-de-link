package com.salessew.adapter.in.web;

import com.salessew.adapter.in.web.dto.CreateUserRequestDTO;
import com.salessew.adapter.in.web.dto.CreateUserResponseDTO;
import com.salessew.core.port.in.CreateUserPortIn;
import com.salessew.core.port.in.DeleteUserPortIn;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/users")
public class UserControllerAdapterIn {

    private final CreateUserPortIn createUserPortIn;
    private final DeleteUserPortIn deleteUserPortIn;

    public UserControllerAdapterIn(CreateUserPortIn createUserPortIn, DeleteUserPortIn deleteUserPortIn) {
        this.createUserPortIn = createUserPortIn;
        this.deleteUserPortIn = deleteUserPortIn;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseDTO> createUser(@RequestBody @Valid CreateUserRequestDTO req) {

        var userCreated = createUserPortIn.execute(req.toDomain());
        var body = CreateUserResponseDTO.fromDomain(userCreated);

        return ResponseEntity.created(URI.create("/")).body(body);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(JwtAuthenticationToken token) {

        var userId = String.valueOf(token.getTokenAttributes().get("sub"));
        deleteUserPortIn.execute(UUID.fromString(userId));

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority(scope_admin)")
    public ResponseEntity<String> authRoute(JwtAuthenticationToken token) {

        var email = token.getTokenAttributes().get("email");
        return ResponseEntity.ok(String.valueOf(email));
    }
}
