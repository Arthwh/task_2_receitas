package com.arthwh.registroreceitas.dto;

public record LoginResponseDto(String token, long expiresIn) {
  public String getToken() {
    return token;
  }

  public long getExpiresIn() {
    return expiresIn;
  }
}
