package com.arthwh.registroReceitas.controller;

import com.arthwh.registroReceitas.dto.LoginDTO;
import com.arthwh.registroReceitas.dto.UsuarioCreateDTO;
import com.arthwh.registroReceitas.model.Usuario;
import com.arthwh.registroReceitas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDto){
        if (loginDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String token = usuarioService.login(loginDto);

        if (token != null){
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/create")
    public ResponseEntity<Usuario> createUsuario(@RequestBody UsuarioCreateDTO usuarioDto){
        if (usuarioDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Usuario usuario = usuarioService.createUsuario(usuarioDto);

        if (usuario != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
