package com.arthwh.registroReceitas.exception;

import com.arthwh.registroReceitas.dto.ReceitaCreateDTO;

public class ReceitaNotFoundException extends RuntimeException {
    public ReceitaNotFoundException(){
        super("Receita não encontrada!");
    }

    public ReceitaNotFoundException(String message) {
        super(message);
    }
}
