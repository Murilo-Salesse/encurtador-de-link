package com.salessew.core.usecase;

import com.salessew.core.exception.UserNotFoundException;
import com.salessew.core.port.in.DeleteUserPortIn;
import com.salessew.core.port.out.UserRepositoryPortOut;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeleteUserUseCase implements DeleteUserPortIn {

    private final UserRepositoryPortOut userRepositoryPortOut;

    public DeleteUserUseCase(UserRepositoryPortOut userRepositoryPortOut) {
        this.userRepositoryPortOut = userRepositoryPortOut;
    }

    @Override
    public void execute(UUID userId) {

        userRepositoryPortOut.findById(userId)
                        .orElseThrow(UserNotFoundException::new);

        userRepositoryPortOut.deleteById(userId);
    }
}
