package com.arthwh.registroreceitas.controller;

import com.arthwh.registroreceitas.dto.UsuarioUpdateDto;
import com.arthwh.registroreceitas.model.Usuario;
import com.arthwh.registroreceitas.service.UsuarioService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
  private final UsuarioService usuarioService;

  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Usuario> getUsuarioById(@PathVariable int id) {
    Usuario usuario = usuarioService.getUsuarioById(id);

    if (usuario != null) {
      return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @GetMapping("/perfil/{login}")
  public ResponseEntity<Usuario> getUsuarioByLogin(@PathVariable String login) {
    Usuario usuario = usuarioService.getUsuarioByLogin(login);

    if (usuario != null) {
      return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @GetMapping
  public List<Usuario> getUsuarios() {
    return usuarioService.getUsuarios();
  }

  @PutMapping
  public ResponseEntity<Usuario> updateUsuario(@RequestBody UsuarioUpdateDto usuarioDto) {
    if (usuarioDto == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Usuario usuario = usuarioService.updateUsuario(usuarioDto);
    if (usuario != null) {
      return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Usuario> deleteUsuario(@PathVariable int id) {
    Usuario usuario = usuarioService.deleteUsuario(id);

    if (usuario != null) {
      return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }
}
