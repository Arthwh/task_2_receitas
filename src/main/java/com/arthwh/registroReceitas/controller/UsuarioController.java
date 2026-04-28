package com.arthwh.registroReceitas.controller;

import com.arthwh.registroReceitas.dto.UsuarioUpdateDTO;
import com.arthwh.registroReceitas.model.Usuario;
import com.arthwh.registroReceitas.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable int id){
        Usuario usuario = usuarioService.getUsuarioById(id);

        if (usuario != null){
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/perfil/{login}")
    public ResponseEntity<Usuario> getUsuarioByLogin(@PathVariable String login){
        Usuario usuario = usuarioService.getUsuarioByLogin(login);

        if (usuario != null){
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public List<Usuario> getUsuarios(){
        return usuarioService.getUsuarios();
    }

    @PutMapping
    public ResponseEntity<Usuario> updateUsuario(@RequestBody UsuarioUpdateDTO usuarioDto){
        if (usuarioDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Usuario usuario = usuarioService.updateUsuario(usuarioDto);
        if (usuario != null){
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> deleteUsuario(@PathVariable int id){
        Usuario usuario =  usuarioService.deleteUsuario(id);

        if (usuario != null){
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
