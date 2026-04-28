package com.arthwh.registroReceitas.dto;

public record LoginResponseDTO(
        String token,
        long expiresIn)
{
    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
