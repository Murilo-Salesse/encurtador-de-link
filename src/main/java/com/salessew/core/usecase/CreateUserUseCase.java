package com.salessew.core.usecase;

import com.salessew.core.domain.User;
import com.salessew.core.exception.UserAlreadyExistException;
import com.salessew.core.port.in.CreateUserPortIn;
import com.salessew.core.port.out.UserRepositoryPortOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreateUserUseCase implements CreateUserPortIn {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserUseCase.class);

    private final UserRepositoryPortOut userRepositoryPortOut;
    private final BCryptPasswordEncoder passwordEncoder;

    public CreateUserUseCase(UserRepositoryPortOut userRepositoryPortOut,  BCryptPasswordEncoder passwordEncoder) {
        this.userRepositoryPortOut = userRepositoryPortOut;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User execute(User user) {

        LOGGER.info("Creating user {}", user.getEmail());

        var optUser = userRepositoryPortOut.findByEmail(user.getEmail());

        if (optUser.isPresent()) {
            throw new UserAlreadyExistException();
        }

        user.encodePassword(passwordEncoder);
        var userCreated = userRepositoryPortOut.save(user);

        LOGGER.info("User created {}", user.getUserId());
        return userCreated;
    }

}
