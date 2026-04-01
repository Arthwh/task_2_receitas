package com.arthwh.registroReceitas.service;

import com.arthwh.registroReceitas.config.TokenCache;
import com.arthwh.registroReceitas.dto.LoginDTO;
import com.arthwh.registroReceitas.dto.UsuarioCreateDTO;
import com.arthwh.registroReceitas.dto.UsuarioUpdateDTO;
import com.arthwh.registroReceitas.model.SituacaoUsuarioEnum;
import com.arthwh.registroReceitas.model.Usuario;
import com.arthwh.registroReceitas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenCache tokenCache;

    public String login(LoginDTO dto){
        Usuario usuario = usuarioRepository.getReferenceByLogin(dto.login());

        if (usuario == null || !passwordEncoder.matches(dto.senha(), usuario.getSenha())){
            throw new BadCredentialsException("Usuário ou senha inválidos!");
        }

        String token = UUID.randomUUID()+"_"+usuario.getId();
        tokenCache.salvarToken(token, usuario.getLogin());
        return token;
    }

    public Usuario getUsuarioById(int id){
        return usuarioRepository.getReferenceById(id);
    }

    public Usuario getUsuarioByLogin(String login){
        return usuarioRepository.getReferenceByLogin(login);
    }

    public List<Usuario> getUsuarios(){
        return usuarioRepository.findAll();
    }

    public Usuario createUsuario(UsuarioCreateDTO usuarioDto){
        Usuario usuario = new Usuario();

        usuario.setNome(usuarioDto.nome());
        String senhaCriptografada = passwordEncoder.encode(usuarioDto.senha());
        usuario.setSenha(senhaCriptografada);
        usuario.setLogin(usuarioDto.login());
        usuario.setSituacao(SituacaoUsuarioEnum.ATIVO);

        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(UsuarioUpdateDTO usuarioDto){
        Usuario usuario = usuarioRepository.getReferenceById(usuarioDto.id());

        if (usuario == null){
            throw new NoSuchElementException("Usuário não encontrado!");
        }

        String senhaCriptografada = passwordEncoder.encode(usuarioDto.senha());
        usuario.setNome(usuarioDto.nome());
        usuario.setSenha(senhaCriptografada);

        return usuarioRepository.save(usuario);
    }

    public Usuario deleteUsuario(int id){
        Usuario usuario = usuarioRepository.getReferenceById(id);

        if (usuario == null){
            return null;
        }

        usuarioRepository.delete(usuario);
        return  usuario;
    }
}
