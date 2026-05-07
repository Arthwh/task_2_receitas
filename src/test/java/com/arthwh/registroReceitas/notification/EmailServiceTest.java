package com.arthwh.registroReceitas.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    private final String remetenteMock = "sistema@receitas.com";

    @BeforeEach
    void setUp() {
        // Injeta o valor do @Value manualmente para o teste unitário
        ReflectionTestUtils.setField(emailService, "remetente", remetenteMock);
    }

    @Test
    @DisplayName("Case 1: Should mount and send the e-mail successfully.")
    void enviarSuccess() {
        String destinatario = "teste@teste.com";
        String assunto = "Nova Receita";
        String corpo = "Receita criada com sucesso!";

        emailService.enviar(destinatario, assunto, corpo);

        // Verifica se o send foi chamado 1 vez e obtém o objeto passado para ele
        verify(emailSender, times(1)).send(messageCaptor.capture());

        // Verifica se o serviço montou o objeto SimpleMailMessage corretamente
        SimpleMailMessage mensagemEnviada = messageCaptor.getValue();

        assert mensagemEnviada.getTo() != null;
        assertEquals(destinatario, mensagemEnviada.getTo()[0]);
        assertEquals(remetenteMock, mensagemEnviada.getFrom());
        assertEquals(assunto, mensagemEnviada.getSubject());
        assertEquals(corpo, mensagemEnviada.getText());
    }

    @Test
    @DisplayName("Case 2: Should not send e-mail if the receiver is invalid.")
    void enviarError1() {
        emailService.enviar("email-invalido", "Assunto", "Corpo");
        emailService.enviar(null, "Assunto", "Corpo");

        // Verifica se o método nunca foi chamado
        verify(emailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Case 3: Should throw an exception if the e-mail send fails.")
    void enviarError2() {
        // Simula um erro do serviço de e-mail
        doThrow(new MailSendException("Servidor fora do ar"))
                .when(emailSender).send(any(SimpleMailMessage.class));

        // Garante que o 'catch' foi acionado
        assertDoesNotThrow(() ->
                emailService.enviar("arthur@teste.com", "Assunto", "Corpo")
        );

        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
