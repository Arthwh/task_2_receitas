package com.arthwh.registroReceitas.repository;

import com.arthwh.registroReceitas.dto.UsuarioRegisterDTO;
import com.arthwh.registroReceitas.model.SituacaoUsuarioEnum;
import com.arthwh.registroReceitas.model.Usuario;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest //Indica para o Spring que será testado uma classe JPA
@ActiveProfiles("test") //Indica pro JUnit que deve usar o properties de teste
class UsuarioRepositoryTest {
    @Autowired
    EntityManager entityManager;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Case 1: Should get user successfully from database.") //Coloca uma descrição para o teste
    void findByLoginSuccess() {
        String login = "arthur.alonso@teste.br";
        String nome = "Arthur Alonso";
        String senha = "senha123";
        //Cria o dto para criar o novo usuário.
        UsuarioRegisterDTO usuarioRegisterDTO = new UsuarioRegisterDTO(nome, login, senha);
        //Cria o novo usuário no db em memória
        Usuario novoUsuario = createUserMock(usuarioRegisterDTO);

        Optional<Usuario> usuarioEncontrado = this.usuarioRepository.findByLogin(novoUsuario.getLogin());

        // Garante que encontrou o usuário
        assertTrue(usuarioEncontrado.isPresent(), "Deveria ter encontrado o usuário");

        // Compara o valor
        assertEquals(login, usuarioEncontrado.get().getLogin());
        assertEquals(nome, usuarioEncontrado.get().getNome());
        assertEquals(senha, usuarioEncontrado.get().getSenha());
    }

    @Test
    @DisplayName("Case 2: Should not get user successfully from database when user not exists.") //Coloca uma descrição para o teste
    void findByLoginError() {
        String login = "arthur.alonso@teste.br";

        //Não insere no DB antes de procurar o usuário
        Optional<Usuario> usuarioEncontrado = this.usuarioRepository.findByLogin(login);

        // Garante que não encontrou o usuário
        assertTrue(usuarioEncontrado.isEmpty(), "Não deveria ter encontrado o usuário");
    }

    //Método auxiliar para inserir um usuário no DB
    private Usuario createUserMock(UsuarioRegisterDTO usuarioRegisterDTO) {
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioRegisterDTO.nome());
        usuario.setSenha(usuarioRegisterDTO.senha());
        usuario.setLogin(usuarioRegisterDTO.login());
        usuario.setSituacao(SituacaoUsuarioEnum.ATIVO);

        this.entityManager.persist(usuario);
        return usuario;
    }
}