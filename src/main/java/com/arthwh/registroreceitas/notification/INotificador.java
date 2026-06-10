package com.arthwh.registroreceitas.notification;

public interface INotificador {
  public void enviar(String destinatario, String assunto, String corpoMensagem);
}
