package com.arthwh.registroReceitas.service;

import com.arthwh.registroReceitas.dto.UsuarioUpdateDTO;
import com.arthwh.registroReceitas.exception.UsuarioNotFoundException;
import com.arthwh.registroReceitas.model.Usuario;
import com.arthwh.registroReceitas.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario getUsuarioById(int id) {
        return usuarioRepository.findById(id)
                .orElseThrow(UsuarioNotFoundException::new);
    }

    public Usuario getUsuarioByLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(UsuarioNotFoundException::new);
    }

    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
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
