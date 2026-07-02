package com.salessew.core.usecase;

import com.salessew.adapter.in.web.dto.LoginRequestDTO;
import com.salessew.adapter.in.web.dto.LoginResponseDTO;
import com.salessew.config.JwtConfig;
import com.salessew.core.exception.LoginException;
import com.salessew.core.port.in.AuthNPortIn;
import com.salessew.core.port.out.UserRepositoryPortOut;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static com.salessew.config.Constants.JWT_EMAIL_CLAIM;

@Component
public class AuthNUseCase implements AuthNPortIn {

    private final UserRepositoryPortOut userRepositoryPortOut;
    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtConfig jwtConfig;

    public AuthNUseCase(UserRepositoryPortOut userRepositoryPortOut, JwtEncoder jwtEncoder,
                        BCryptPasswordEncoder bCryptPasswordEncoder, JwtConfig jwtConfig) {
        this.userRepositoryPortOut = userRepositoryPortOut;
        this.jwtEncoder = jwtEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public LoginResponseDTO execute(LoginRequestDTO req) {

        var user = this.userRepositoryPortOut.findByEmail(req.email())
                .orElseThrow(LoginException::new);

        var isPasswordValid = bCryptPasswordEncoder.matches(req.password(), user.getPassword());

        if (!isPasswordValid) {throw new LoginException();}

        var expiresIn = jwtConfig.getExpiresIn();
        var claims = JwtClaimsSet.builder()
                .subject(user.getUserId().toString())
                .issuer(jwtConfig.getIssuer())
                .claim(JWT_EMAIL_CLAIM, user.getEmail())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .build();

        var tokenJwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDTO(tokenJwt, expiresIn);
    }
}
