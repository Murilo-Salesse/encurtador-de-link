package com.salessew.adapter.in.web.dto;

import com.salessew.core.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateUserRequestDTO(@NotBlank @Email @Length(max = 100)  String email,
                                   @NotBlank @Length(min = 8, max = 64) String password,
                                   @NotBlank @Length(min = 5, max = 50) String nickname) {

    public User toDomain(){
        return new User(email, password, nickname);
    }
}
