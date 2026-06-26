package com.salessew.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserNotFoundException extends DomainException {

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("User Not Found.");
        pb.setDetail("User Not Found Exception");

        return pb;
    }
}
