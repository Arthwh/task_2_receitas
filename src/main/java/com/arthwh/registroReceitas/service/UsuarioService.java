package com.arthwh.registroReceitas.service;

import com.arthwh.registroReceitas.config.TokenCache;
import com.arthwh.registroReceitas.dto.LoginDTO;
import com.arthwh.registroReceitas.dto.UsuarioCreateDTO;
import com.arthwh.registroReceitas.dto.UsuarioUpdateDTO;
import com.arthwh.registroReceitas.exception.UsuarioNotFoundException;
import com.arthwh.registroReceitas.model.SituacaoUsuarioEnum;
import com.arthwh.registroReceitas.model.Usuario;
import com.arthwh.registroReceitas.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenCache tokenCache;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, TokenCache tokenCache) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenCache = tokenCache;
    }

    public String login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByLogin(dto.login())
                .orElseThrow(UsuarioNotFoundException::new);

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new BadCredentialsException("Usuário ou senha inválidos!");
        }

        String token = UUID.randomUUID() + "_" + usuario.getId();
        tokenCache.salvarToken(token, usuario.getLogin());
        log.info("Login realizado com sucesso para o usuário: {}", dto.login());
        return token;
    }

    public Usuario getUsuarioById(int id) {
        return usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
    }

    public Usuario getUsuarioByLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(UsuarioNotFoundException::new);
    }

    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario createUsuario(UsuarioCreateDTO usuarioDto) {
        Usuario usuario = new Usuario();

        usuario.setNome(usuarioDto.nome());
        String senhaCriptografada = passwordEncoder.encode(usuarioDto.senha());
        usuario.setSenha(senhaCriptografada);
        usuario.setLogin(usuarioDto.login());
        usuario.setSituacao(SituacaoUsuarioEnum.ATIVO);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        log.info("Usuário {} criado com sucesso. ID: {}", usuarioSalvo.getLogin(), usuarioSalvo.getId());

        return usuarioSalvo;
    }

    public Usuario updateUsuario(UsuarioUpdateDTO usuarioDto) {
        Usuario usuario = usuarioRepository.findById(usuarioDto.id())
                .orElseThrow(UsuarioNotFoundException::new);

        String senhaCriptografada = passwordEncoder.encode(usuarioDto.senha());
        usuario.setNome(usuarioDto.nome());
        usuario.setSenha(senhaCriptografada);

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        log.info("Usuário ID: {} atualizado com sucesso.", usuarioAtualizado.getId());

        return usuarioAtualizado;
    }

    public Usuario deleteUsuario(int id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNotFoundException::new);

        usuarioRepository.delete(usuario);
        log.info("Usuário ID: {} excluído com sucesso.", id);

        return usuario;
    }
}
