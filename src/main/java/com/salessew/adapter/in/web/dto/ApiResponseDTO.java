package com.salessew.adapter.in.web.dto;

import java.util.List;

public record ApiResponseDTO<T>(List<T> data,
                                String nextToken) {
}
