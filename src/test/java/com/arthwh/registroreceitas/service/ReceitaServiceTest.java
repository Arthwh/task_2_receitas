package com.arthwh.registroreceitas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.arthwh.registroreceitas.dto.ReceitaRegisterDto;
import com.arthwh.registroreceitas.dto.ReceitaUpdateDto;
import com.arthwh.registroreceitas.event.ReceitaAtualizadaEvent;
import com.arthwh.registroreceitas.event.ReceitaCriadaEvent;
import com.arthwh.registroreceitas.exception.ReceitaNotFoundException;
import com.arthwh.registroreceitas.model.Receita;
import com.arthwh.registroreceitas.model.TipoReceitaEnum;
import com.arthwh.registroreceitas.repository.ReceitaRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test") // Indica pro JUnit que deve usar o properties de teste
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
    // Prepara os objeto
    Receita receitaSalvaMock = criarReceitaMock();
    ReceitaRegisterDto receitaRegisterDto =
        new ReceitaRegisterDto(
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
    BigDecimal novoCusto = BigDecimal.valueOf(30.00);
    Receita receitaEncontradaMock = criarReceitaMock();
    ReceitaUpdateDto receitaUpdateDTO =
        new ReceitaUpdateDto(receitaEncontradaMock.getId(), novaDescricao, novoCusto);

    when(receitaRepository.findById(receitaEncontradaMock.getId()))
        .thenReturn(Optional.of(receitaEncontradaMock));
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
    // Cria um dto falso (não importa os dados, pois não será validado)
    ReceitaUpdateDto receitaUpdateDTO =
        new ReceitaUpdateDto(1, "Teste de erro update receita", BigDecimal.valueOf(15.00));

    when(receitaRepository.findById(1))
        .thenReturn(
            Optional
                .empty()); // Retorna um Optional vazio, simulando que o usuário não foi encontrado

    // Verifica se a classe lançou uma excessão ReceitaNotFoundException
    assertThrows(
        ReceitaNotFoundException.class, () -> receitaService.updateReceita(receitaUpdateDTO));

    verify(receitaRepository, times(1)).findById(receitaUpdateDTO.id());
    // Verifica se os métodos nunca foram chamados, confirmando que o código parou, pois a receita
    // não foi encontrada
    verify(receitaRepository, never()).save(any(Receita.class));
    verify(publisher, never()).publishEvent(any(ReceitaAtualizadaEvent.class));
  }

  private Receita criarReceitaMock() {
    Receita receita = new Receita();
    receita.setId(1);
    receita.setNome("Bolo");
    receita.setDescricao("Bolo de chocolate");
    receita.setCusto(BigDecimal.valueOf(25.50));
    receita.setTipoReceita(TipoReceitaEnum.DOCE);

    return receita;
  }
}
