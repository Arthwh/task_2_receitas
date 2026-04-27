package com.arthwh.registroReceitas.notification;

public interface INotificador {
    public void enviar(String destinatario, String assunto, String corpoMensagem);
}
