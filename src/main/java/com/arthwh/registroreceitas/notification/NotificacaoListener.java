package com.arthwh.registroreceitas.notification;

import com.arthwh.registroreceitas.event.ReceitaAtualizadaEvent;
import com.arthwh.registroreceitas.event.ReceitaCriadaEvent;
import com.arthwh.registroreceitas.model.Usuario;
import com.arthwh.registroreceitas.repository.UsuarioRepository;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoListener {
  private final List<INotificador> notificadores;
  private final UsuarioRepository usuarioRepository;

  public NotificacaoListener(
      List<INotificador> notificadores, UsuarioRepository usuarioRepository) {
    this.notificadores = notificadores;
    this.usuarioRepository = usuarioRepository;
  }

  @Async
  @EventListener
  public void aoCriarReceita(ReceitaCriadaEvent evento) {
    String assunto = "Nova Receita";
    String mensagem = "A receita " + evento.nomeReceita() + " foi criada!";

    getUsuariosQueremNotificacao()
        .forEach(usuario -> enviaMensagemNotificadorPadrao(usuario, assunto, mensagem));
  }

  @Async
  @EventListener
  public void aoAtualizarReceita(ReceitaAtualizadaEvent evento) {
    String assunto = "Receita Atualizada";
    String mensagem = "A receita " + evento.nomeReceita() + " foi atualizada!";

    getUsuariosQueremNotificacao()
        .forEach(usuario -> enviaMensagemNotificadorPadrao(usuario, assunto, mensagem));
  }

  private List<Usuario> getUsuariosQueremNotificacao() {
    // Busca todos os usuários que querem notificações
    // TODO: implementar querer receber notificação
    //        List<Usuario> interessados = usuarioRepository.findByReceberNotificacoesTrue();
    // Busca todos os usuários pois o metodo acima não está implementado
    List<Usuario> interessados = usuarioRepository.findAll();
    return interessados;
  }

  private void enviaMensagemNotificadorPadrao(Usuario usuario, String assunto, String mensagem) {
    // Procura na lista qual notificador atende a preferência do usuário
    notificadores.stream()
        //                    .filter(n -> n.suporta(user.getPreferenciaNotificacao()))
        .findFirst()
        .ifPresent(n -> n.enviar(usuario.getLogin(), assunto, mensagem));
  }
}
