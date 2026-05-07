package com.arthwh.registroReceitas.service;

import com.arthwh.registroReceitas.dto.ReceitaRegisterDTO;
import com.arthwh.registroReceitas.dto.ReceitaUpdateDTO;
import com.arthwh.registroReceitas.event.ReceitaAtualizadaEvent;
import com.arthwh.registroReceitas.event.ReceitaCriadaEvent;
import com.arthwh.registroReceitas.exception.ReceitaNotFoundException;
import com.arthwh.registroReceitas.model.Receita;
import com.arthwh.registroReceitas.model.TipoReceitaEnum;
import com.arthwh.registroReceitas.repository.ReceitaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test") //Indica pro JUnit que deve usar o properties de teste
class ReceitaServiceTest {
    @Mock // Cria um repositório falso
    private ReceitaRepository receitaRepository;

    @Mock // Cria um disparador de eventos falso
    private ApplicationEventPublisher publisher;

    @InjectMocks // Pega os Mocks acima e joga para dentro do Service
    private ReceitaService receitaService;

    @Test
    @DisplayName("Case 1: Should create an recipe and event publication successfully.")
    void createReceitaSuccess() {
        //Prepara os objeto
        Receita receitaSalvaMock = criarReceitaMock();
        ReceitaRegisterDTO receitaRegisterDto = new ReceitaRegisterDTO(
                receitaSalvaMock.getNome(),
                receitaSalvaMock.getDescricao(),
                receitaSalvaMock.getCusto(),
                receitaSalvaMock.getTipoReceita());

        // Diz ao mockito o que retornar em um método de uma classe mockada
        when(receitaRepository.save(any(Receita.class))).thenReturn(receitaSalvaMock);

        Receita resultado = receitaService.createReceita(receitaRegisterDto);

        // Garante que o método retornou o objeto certo
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(receitaRegisterDto.nome(), resultado.getNome());
        assertEquals(receitaRegisterDto.descricao(), resultado.getDescricao());
        assertEquals(receitaRegisterDto.custo(), resultado.getCusto());
        assertEquals(receitaRegisterDto.tipoReceita(), resultado.getTipoReceita());

        // Garante que o repositório foi chamado exatamente 1 vez
        verify(receitaRepository, times(1)).save(any(Receita.class));

        // Garante que o evento foi disparado exatamente 1 vez com o evento correto
        verify(publisher, times(1)).publishEvent(any(ReceitaCriadaEvent.class));
    }

    @Test
    @DisplayName("Case 2: Should update an recipe and create event publication successfully.")
    void updateReceitaSuccess() {
        String novaDescricao = "Bolo de cenoura";
        Double novoCusto = 30.00;
        Receita receitaEncontradaMock = criarReceitaMock();
        ReceitaUpdateDTO receitaUpdateDTO = new ReceitaUpdateDTO(
                receitaEncontradaMock.getId(),
                novaDescricao,
                novoCusto);

        when(receitaRepository.findById(receitaEncontradaMock.getId())).thenReturn(Optional.of(receitaEncontradaMock));
        when(receitaRepository.save(any(Receita.class))).thenReturn(receitaEncontradaMock);

        Receita receitaAtualizada = receitaService.updateReceita(receitaUpdateDTO);

        // Garante que o método retornou o objeto certo
        assertNotNull(receitaAtualizada);
        assertEquals(receitaEncontradaMock.getId(), receitaAtualizada.getId());
        assertEquals(receitaEncontradaMock.getNome(), receitaAtualizada.getNome());
        assertEquals(novaDescricao, receitaAtualizada.getDescricao()); // Verifica se alterou
        assertEquals(novoCusto, receitaAtualizada.getCusto()); // Verifica se alterou
        assertEquals(receitaEncontradaMock.getTipoReceita(), receitaAtualizada.getTipoReceita());

        verify(receitaRepository, times(1)).findById(receitaEncontradaMock.getId());
        verify(receitaRepository, times(1)).save(any(Receita.class));
        verify(publisher, times(1)).publishEvent(any(ReceitaAtualizadaEvent.class));
    }

    @Test
    @DisplayName("Case 3: Should not update an recipe and must throw an ReceitaNotFoundException.")
    void updateReceitaError() {
        //Cria um dto falso (não importa os dados, pois não será validado)
        ReceitaUpdateDTO receitaUpdateDTO = new ReceitaUpdateDTO(1, "Teste de erro update receita", 10.00);

        when(receitaRepository.findById(1)).thenReturn(Optional.empty()); //Retorna um Optional vazio, simulando que o usuário não foi encontrado

        //Verifica se a classe lançou uma excessão ReceitaNotFoundException
        assertThrows(
                ReceitaNotFoundException.class,
                () -> receitaService.updateReceita(receitaUpdateDTO)
        );

        verify(receitaRepository, times(1)).findById(receitaUpdateDTO.id());
        //Verifica se os métodos nunca foram chamados, confirmando que o código parou, pois a receita não foi encontrada
        verify(receitaRepository, never()).save(any(Receita.class));
        verify(publisher, never()).publishEvent(any(ReceitaAtualizadaEvent.class));
    }

    private Receita criarReceitaMock() {
        Receita receita = new Receita();
        receita.setId(1);
        receita.setNome("Bolo");
        receita.setDescricao("Bolo de chocolate");
        receita.setCusto(25.50);
        receita.setTipoReceita(TipoReceitaEnum.DOCE);

        return receita;
    }
}
