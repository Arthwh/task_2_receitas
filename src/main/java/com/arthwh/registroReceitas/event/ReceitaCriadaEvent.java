package com.arthwh.registroReceitas.event;

public record ReceitaCriadaEvent(
        int receitaId,
        String nomeReceita) {
}
