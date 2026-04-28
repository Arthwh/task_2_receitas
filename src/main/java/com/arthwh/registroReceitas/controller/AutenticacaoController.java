package com.arthwh.registroReceitas.controller;

import com.arthwh.registroReceitas.dto.LoginResponseDTO;
import com.arthwh.registroReceitas.dto.UsuarioLoginDTO;
import com.arthwh.registroReceitas.dto.UsuarioRegisterDTO;
import com.arthwh.registroReceitas.model.Usuario;
import com.arthwh.registroReceitas.service.AutenticacaoService;
import com.arthwh.registroReceitas.service.JwtService;
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
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UsuarioLoginDTO loginDto) {
        if (loginDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Usuario usuarioAutenticado = autenticacaoService.authenticate(loginDto);
        String token = jwtService.generateToken(usuarioAutenticado);

        if (token != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(token, jwtService.getExpirationTime()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Usuario> createUsuario(@RequestBody UsuarioRegisterDTO usuarioRegisterDto) {
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
