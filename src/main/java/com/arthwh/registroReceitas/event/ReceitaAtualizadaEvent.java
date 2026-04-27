package com.arthwh.registroReceitas.event;

public record ReceitaAtualizadaEvent(
        int receitaId,
        String nomeReceita) {
}
