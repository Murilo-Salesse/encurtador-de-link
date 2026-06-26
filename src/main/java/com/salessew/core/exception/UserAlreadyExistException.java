package com.salessew.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserAlreadyExistException extends DomainException {

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("User Already Exist.");
        pb.setDetail("There is a user with this email. Try another email");

        return pb;
    }
}
