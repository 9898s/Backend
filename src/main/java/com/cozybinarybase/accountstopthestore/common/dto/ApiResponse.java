package com.cozybinarybase.accountstopthestore.common.dto;

import com.cozybinarybase.accountstopthestore.common.handler.type.StatusType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  private final StatusType status;
  private final String message;
  private final T data;

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(StatusType.SUCCESS, null, data);
  }

  public static <T> ApiResponse<T> fail(T data) {
    return new ApiResponse<>(StatusType.FAIL, null, data);
  }

  public static <T> ApiResponse<T> error(String message) {
    return new ApiResponse<>(StatusType.ERROR, message, null);
  }
}
