package com.salessew.core.usecase;

import com.salessew.adapter.in.web.dto.LoginRequestDTO;
import com.salessew.adapter.in.web.dto.LoginResponseDTO;
import com.salessew.core.exception.LoginException;
import com.salessew.core.port.in.AuthNPortIn;
import com.salessew.core.port.out.UserRepositoryPortOut;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuthNUseCase implements AuthNPortIn {

    private final UserRepositoryPortOut userRepositoryPortOut;
    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthNUseCase(UserRepositoryPortOut userRepositoryPortOut, JwtEncoder jwtEncoder,
                        BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepositoryPortOut = userRepositoryPortOut;
        this.jwtEncoder = jwtEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public LoginResponseDTO execute(LoginRequestDTO req) {

        var user = this.userRepositoryPortOut.findByEmail(req.email())
                .orElseThrow(LoginException::new);

        var isPasswordValid = bCryptPasswordEncoder.matches(req.password(), user.getPassword());

        if (!isPasswordValid) {throw new LoginException();}

        var expiresIn = 300L;
        var claims = JwtClaimsSet.builder()
                .subject(user.getUserId().toString())
                .issuer("linkshortener")
                .claim("email", user.getEmail())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .build();

        var tokenJwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDTO(tokenJwt, expiresIn);
    }
}
