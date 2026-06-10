package com.arthwh.registroreceitas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.arthwh.registroreceitas.dto.UsuarioUpdateDto;
import com.arthwh.registroreceitas.exception.UsuarioNotFoundException;
import com.arthwh.registroreceitas.model.SituacaoUsuarioEnum;
import com.arthwh.registroreceitas.model.Usuario;
import com.arthwh.registroreceitas.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test") // Indica pro JUnit que deve usar o properties de teste
class UsuarioServiceTest {
  @Mock private UsuarioRepository usuarioRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @InjectMocks private UsuarioService usuarioService;

  @Test
  @DisplayName("Case 1: Should update an user successfully.")
  void updateUsuarioSuccess() {
    String novoNome = "Usuario atualizado";
    String novaSenha = "senha atualizada";
    Usuario usuarioEncontrado = criarUsuarioMock();
    UsuarioUpdateDto usuarioUpdateDTO =
        new UsuarioUpdateDto(usuarioEncontrado.getId(), novoNome, novaSenha);

    when(usuarioRepository.findById(usuarioUpdateDTO.id()))
        .thenReturn(Optional.of(usuarioEncontrado));
    when(passwordEncoder.encode(usuarioUpdateDTO.senha())).thenReturn(usuarioUpdateDTO.senha());
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEncontrado);

    Usuario usuarioAtualizado = usuarioService.updateUsuario(usuarioUpdateDTO);

    assertNotNull(usuarioAtualizado);
    assertEquals(usuarioEncontrado.getId(), usuarioAtualizado.getId());
    assertEquals(novoNome, usuarioAtualizado.getNome());
    assertEquals(usuarioEncontrado.getLogin(), usuarioAtualizado.getLogin());
    assertEquals(novaSenha, usuarioAtualizado.getSenha());
    assertEquals(usuarioEncontrado.getSituacao(), usuarioAtualizado.getSituacao());

    verify(usuarioRepository, times(1)).findById(usuarioUpdateDTO.id());
    verify(passwordEncoder, times(1)).encode(usuarioUpdateDTO.senha());
    verify(usuarioRepository, times(1)).save(any(Usuario.class));
  }

  @Test
  @DisplayName("Case 2: Should not update an user successfully.")
  void updateUsuarioError() {
    UsuarioUpdateDto usuarioUpdateDTO =
        new UsuarioUpdateDto(1, "Teste de erro update usuário", "Nova senha");

    when(usuarioRepository.findById(usuarioUpdateDTO.id()))
        .thenReturn(
            Optional.empty()); // Retorna um optional vazio para simular que o usuário não foi
    // encontrado

    // Verifica se a classe lançou uma excessão UsuarioNotFoundException
    assertThrows(
        UsuarioNotFoundException.class, () -> usuarioService.updateUsuario(usuarioUpdateDTO));

    verify(usuarioRepository, times(1)).findById(usuarioUpdateDTO.id());
    // Verifica se os métodos nunca foram chamados, confirmando que o código parou, pois o usuário
    // não foi encontrado
    verify(passwordEncoder, never()).encode(usuarioUpdateDTO.senha());
    verify(usuarioRepository, never()).save(any(Usuario.class));
  }

  private Usuario criarUsuarioMock() {
    Usuario usuarioMock = new Usuario();
    usuarioMock.setId(1);
    usuarioMock.setNome("Usuário Teste");
    usuarioMock.setLogin("usuario@teste.com");
    usuarioMock.setSenha("senha123");
    usuarioMock.setSituacao(SituacaoUsuarioEnum.ATIVO);

    return usuarioMock;
  }
}
