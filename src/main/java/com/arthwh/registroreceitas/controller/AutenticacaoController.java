package com.arthwh.registroreceitas.controller;

import com.arthwh.registroreceitas.dto.LoginResponseDto;
import com.arthwh.registroreceitas.dto.UsuarioLoginDto;
import com.arthwh.registroreceitas.dto.UsuarioRegisterDto;
import com.arthwh.registroreceitas.model.Usuario;
import com.arthwh.registroreceitas.service.AutenticacaoService;
import com.arthwh.registroreceitas.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
  private final AutenticacaoService autenticacaoService;
  private final JwtService jwtService;

  public AutenticacaoController(AutenticacaoService autenticacaoService, JwtService jwtService) {
    this.autenticacaoService = autenticacaoService;
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody UsuarioLoginDto loginDto) {
    if (loginDto == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Usuario usuarioAutenticado = autenticacaoService.authenticate(loginDto);
    if (usuarioAutenticado == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String token = jwtService.generateToken(usuarioAutenticado);

    if (token != null) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(new LoginResponseDto(token, jwtService.getExpirationTime()));
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

  @PostMapping("/signup")
  public ResponseEntity<Usuario> createUsuario(@RequestBody UsuarioRegisterDto usuarioRegisterDto) {
    if (usuarioRegisterDto == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      Usuario usuarioRegistrado = autenticacaoService.signup(usuarioRegisterDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRegistrado);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
