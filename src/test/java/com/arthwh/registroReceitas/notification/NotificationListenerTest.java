package com.arthwh.registroReceitas.notification;

import com.arthwh.registroReceitas.event.ReceitaAtualizadaEvent;
import com.arthwh.registroReceitas.event.ReceitaCriadaEvent;
import com.arthwh.registroReceitas.model.SituacaoUsuarioEnum;
import com.arthwh.registroReceitas.model.Usuario;
import com.arthwh.registroReceitas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationListenerTest {
    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    INotificador notificadorMock;

    NotificacaoListener notificacaoListener;

    @BeforeEach
    void setUp() {
        notificacaoListener = new NotificacaoListener(List.of(notificadorMock), usuarioRepository);
    }

    @Test
    @DisplayName("Case 1: Should receive the publisher notification when an recipe is created, and call the notificator successfully.")
    void aoCriarReceitaSuccess() {
        String nomeReceitaFalsa = "Receita falsa";
        //Cria um evento falso
        ReceitaCriadaEvent evento = new ReceitaCriadaEvent(1, nomeReceitaFalsa);
        Usuario usuarioMock = criarUsuarioMock();

        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioMock));

        notificacaoListener.aoCriarReceita(evento);

        // Verifica se o repositório foi chamado
        verify(usuarioRepository, times(1)).findAll();

        // Verifica se o método 'enviar' foi acionado com os dados esperados
        verify(notificadorMock, times(1)).enviar(
                eq(usuarioMock.getLogin()),
                eq("Nova Receita"),
                any(String.class));
    }

    @Test
    @DisplayName("Case 2: Should receive the publisher notification when an recipe is updated, and call the notificator successfully.")
    void aoAtualizarReceitaSuccess() {
        String nomeReceitaFalsa = "Receita falsa";
        //Cria um evento falso
        ReceitaAtualizadaEvent evento = new ReceitaAtualizadaEvent(1, nomeReceitaFalsa);
        Usuario usuarioMock = criarUsuarioMock();

        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioMock));

        notificacaoListener.aoAtualizarReceita(evento);

        // Verifica se o repositório foi chamado
        verify(usuarioRepository, times(1)).findAll();

        // Verifica se o método 'enviar' foi acionado com os dados esperados
        verify(notificadorMock, times(1)).enviar(
                eq(usuarioMock.getLogin()),
                eq("Receita Atualizada"),
                any(String.class));
    }

    private Usuario criarUsuarioMock() {
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1);
        usuarioMock.setNome("Usuário Teste");
        usuarioMock.setLogin("usuario@teste.com");
        usuarioMock.setSenha("senha123");
        usuarioMock.setSituacao(SituacaoUsuarioEnum.ATIVO);

        return usuarioMock;
    }
}
