package com.arthwh.registroReceitas.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailService implements INotificador {
    private final JavaMailSender emailSender;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    @Value("${spring.mail.username}")
    private String remetente;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void enviar(String destinatario, String assunto, String corpoMensagem) {
        if (!isValidEmail(destinatario)) {
            System.out.println("Email " + destinatario + " inválido!");
            return;
        }

        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setTo(destinatario);
            mensagem.setFrom(remetente);
            mensagem.setSubject(assunto);
            mensagem.setText(corpoMensagem);
            emailSender.send(mensagem);
        } catch (MailException e) {
            System.out.println("Erro ao enviar email: " + e.getMessage());
        }
    }

    private static boolean isValidEmail(String email) {
        if (email == null) return false;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
