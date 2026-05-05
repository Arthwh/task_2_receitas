package com.arthwh.registroReceitas.service;

import com.arthwh.registroReceitas.dto.UsuarioLoginDTO;
import com.arthwh.registroReceitas.dto.UsuarioRegisterDTO;
import com.arthwh.registroReceitas.exception.UsuarioNotFoundException;
import com.arthwh.registroReceitas.model.SituacaoUsuarioEnum;
import com.arthwh.registroReceitas.model.Usuario;
import com.arthwh.registroReceitas.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.InvalidPropertiesFormatException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AutenticacaoService autenticacaoService;

    @Test
    @DisplayName("Should create an user successfully.")
    void signupSuccess() throws InvalidPropertiesFormatException {
        Usuario usuarioSalvoMock = criarUsuarioMock();
        UsuarioRegisterDTO usuarioASerSalvo = new UsuarioRegisterDTO(
                usuarioSalvoMock.getNome(),
                usuarioSalvoMock.getLogin(),
                usuarioSalvoMock.getSenha());

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvoMock);
        when(passwordEncoder.encode(usuarioSalvoMock.getSenha())).thenReturn(usuarioSalvoMock.getSenha());

        Usuario usuarioRetornado = autenticacaoService.signup(usuarioASerSalvo);

        assertNotNull(usuarioRetornado);
        assertEquals(usuarioASerSalvo.nome(), usuarioRetornado.getNome());
        assertEquals(usuarioASerSalvo.login(), usuarioRetornado.getLogin());
        assertEquals(usuarioASerSalvo.senha(), usuarioRetornado.getSenha());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Should authenticate an user successfully.")
    void authenticateSuccess() {
        //Cria os objetos usados no teste
        Usuario usuarioAutenticado = criarUsuarioMock();
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO(
                usuarioAutenticado.getLogin(),
                usuarioAutenticado.getSenha());

        //Define o comportamento do método chamado
        when(usuarioRepository.findByLogin(loginDTO.login())).thenReturn(Optional.of(usuarioAutenticado));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(loginDTO.login(), loginDTO.senha()));

        Usuario usuarioRetornado = autenticacaoService.authenticate(loginDTO);

        //Verifica se o usuário retornado é o mesmo que o enviado
        assertNotNull(usuarioRetornado);
        assertEquals(loginDTO.login(), usuarioRetornado.getLogin());
        assertEquals(loginDTO.senha(), usuarioRetornado.getSenha());

        verify(usuarioRepository, times(1)).findByLogin(loginDTO.login());
    }

    @Test
    @DisplayName("Should not authenticate an user successfully.")
    void authenticateError() {
        //Cria os objetos usados no teste
        Usuario usuarioAutenticado = criarUsuarioMock();
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO(
                usuarioAutenticado.getLogin(),
                usuarioAutenticado.getSenha());

        //Define o comportamento do método chamado
        when(usuarioRepository.findByLogin(loginDTO.login())).thenReturn(Optional.empty()); //Retorna um optional vazio, simulando que o usuário não foi encontrado
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(loginDTO.login(), loginDTO.senha()));

        //Verifica se foi lançada uma excessão UsuarioNotFoundException
        assertThrows(
                UsuarioNotFoundException.class,
                () -> autenticacaoService.authenticate(loginDTO)
        );

        verify(usuarioRepository, times(1)).findByLogin(loginDTO.login());
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
