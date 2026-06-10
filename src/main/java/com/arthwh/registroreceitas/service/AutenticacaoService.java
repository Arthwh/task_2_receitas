package com.arthwh.registroreceitas.service;

import com.arthwh.registroreceitas.dto.UsuarioLoginDto;
import com.arthwh.registroreceitas.dto.UsuarioRegisterDto;
import com.arthwh.registroreceitas.exception.UsuarioNotFoundException;
import com.arthwh.registroreceitas.model.SituacaoUsuarioEnum;
import com.arthwh.registroreceitas.model.Usuario;
import com.arthwh.registroreceitas.repository.UsuarioRepository;
import java.util.InvalidPropertiesFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AutenticacaoService {
  private final UsuarioRepository usuarioRepository;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  public AutenticacaoService(
      UsuarioRepository usuarioRepository,
      AuthenticationManager authenticationManager,
      PasswordEncoder passwordEncoder) {
    this.authenticationManager = authenticationManager;
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Usuario signup(UsuarioRegisterDto usuarioRegisterDTO)
      throws InvalidPropertiesFormatException {
    if (!isValidLogin(usuarioRegisterDTO.login())) {
      throw new InvalidPropertiesFormatException("O login precisa ser um e-mail!");
    }

    Usuario usuario = new Usuario();

    usuario.setNome(usuarioRegisterDTO.nome());
    String senhaCriptografada = hashPassword(usuarioRegisterDTO.senha());
    usuario.setSenha(senhaCriptografada);
    usuario.setLogin(usuarioRegisterDTO.login());
    usuario.setSituacao(SituacaoUsuarioEnum.ATIVO);

    Usuario usuarioSalvo = usuarioRepository.save(usuario);
    log.info(
        "Usuário {} criado com sucesso. ID: {}", usuarioSalvo.getLogin(), usuarioSalvo.getId());

    return usuarioSalvo;
  }

  public Usuario authenticate(UsuarioLoginDto input) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(input.login(), input.senha()));

    return usuarioRepository.findByLogin(input.login()).orElseThrow(UsuarioNotFoundException::new);
  }

  // Valida se o login é um e-mail
  private boolean isValidLogin(String login) {
    String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    Pattern pattern = Pattern.compile(EMAIL_REGEX);

    if (login == null) {return false;}
    Matcher matcher = pattern.matcher(login);
    return matcher.matches();
  }

  public String hashPassword(String senha) {
    return passwordEncoder.encode(senha);
  }
}
