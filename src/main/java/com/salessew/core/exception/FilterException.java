package com.salessew.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.Map;

public class FilterException extends DomainException {

    private Map<String, Object> invalidFilters;

    public FilterException(Map<String, Object> invalidFilters) {
        this.invalidFilters = invalidFilters;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("There is a problem with the filters.");
        pb.setDetail("Check the validations field.");

        pb.setProperties(Map.of("validations", invalidFilters));

        return pb;
    }
}
