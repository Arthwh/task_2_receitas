package com.arthwh.registroReceitas.repository;

import com.arthwh.registroReceitas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario getReferenceByLogin(String login);
}
