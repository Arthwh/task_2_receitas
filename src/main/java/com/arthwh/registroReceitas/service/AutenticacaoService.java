package com.arthwh.registroReceitas.service;

import com.arthwh.registroReceitas.dto.UsuarioLoginDTO;
import com.arthwh.registroReceitas.dto.UsuarioRegisterDTO;
import com.arthwh.registroReceitas.exception.UsuarioNotFoundException;
import com.arthwh.registroReceitas.model.SituacaoUsuarioEnum;
import com.arthwh.registroReceitas.model.Usuario;
import com.arthwh.registroReceitas.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.InvalidPropertiesFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class AutenticacaoService {
    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public AutenticacaoService(
            UsuarioRepository usuarioRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario signup(UsuarioRegisterDTO usuarioRegisterDTO) throws InvalidPropertiesFormatException {
        if (!isValidLogin(usuarioRegisterDTO.login())){
            throw new InvalidPropertiesFormatException("O login precisa ser um e-mail!");
        }

        Usuario usuario = new Usuario();

        usuario.setNome(usuarioRegisterDTO.nome());
        String senhaCriptografada = passwordEncoder.encode(usuarioRegisterDTO.senha());
        usuario.setSenha(senhaCriptografada);
        usuario.setLogin(usuarioRegisterDTO.login());
        usuario.setSituacao(SituacaoUsuarioEnum.ATIVO);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        log.info("Usuário {} criado com sucesso. ID: {}", usuarioSalvo.getLogin(), usuarioSalvo.getId());

        return usuarioSalvo;
    }

    public Usuario authenticate(UsuarioLoginDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.login(),
                        input.senha()
                )
        );

        return usuarioRepository.findByLogin(input.login())
                .orElseThrow(UsuarioNotFoundException::new);
    }

    //Valida se o login é um e-mail
    private boolean isValidLogin(String login){
        if (login == null) return false;
        Matcher matcher = pattern.matcher(login);
        return matcher.matches();
    }
}