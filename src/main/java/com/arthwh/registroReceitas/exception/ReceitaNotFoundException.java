package com.arthwh.registroReceitas.exception;

public class ReceitaNotFoundException extends RuntimeException {
    public ReceitaNotFoundException(){
        super("Receita não encontrada!");
    }

    public ReceitaNotFoundException(String message) {
        super(message);
    }
}
