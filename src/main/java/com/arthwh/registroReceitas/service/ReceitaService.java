package com.arthwh.registroReceitas.service;

import com.arthwh.registroReceitas.dto.ReceitaRegisterDTO;
import com.arthwh.registroReceitas.dto.ReceitaUpdateDTO;
import com.arthwh.registroReceitas.event.ReceitaAtualizadaEvent;
import com.arthwh.registroReceitas.event.ReceitaCriadaEvent;
import com.arthwh.registroReceitas.exception.ReceitaNotFoundException;
import com.arthwh.registroReceitas.model.Receita;
import com.arthwh.registroReceitas.model.TipoReceitaEnum;
import com.arthwh.registroReceitas.repository.ReceitaRepository;
import com.arthwh.registroReceitas.repository.ReceitaSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class ReceitaService {
    private final ReceitaRepository receitaRepository;
    private final ApplicationEventPublisher publisher;

    public ReceitaService(ReceitaRepository receitaRepository, ApplicationEventPublisher publisher) {
        this.receitaRepository = receitaRepository;
        this.publisher = publisher;
    }

    public Receita getReceitaById(int id){
        return receitaRepository.findById(id)
                .orElseThrow(ReceitaNotFoundException::new);
    }

    public List<Receita> getReceitas(){
        return  receitaRepository.findAll();
    }

    public List<Receita> getReceitasComFiltros(TipoReceitaEnum tipoReceita, LocalDate dataInicio){
        //Query vazia
        Specification<Receita> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (tipoReceita != null) {
            specification = specification.and(ReceitaSpecification.hasRecipeType(tipoReceita));
        }

        if (dataInicio != null) {
            specification = specification.and(ReceitaSpecification.hasInitialDate(dataInicio));
        }

        return  receitaRepository.findAll(specification);
    }

    public Receita createReceita(ReceitaRegisterDTO receitaDto){
        Receita receita = new Receita();

        receita.setNome(receitaDto.nome());
        receita.setDescricao(receitaDto.descricao());
        receita.setCusto(receitaDto.custo());
        receita.setTipoReceita(receitaDto.tipoReceita());

        Receita novaReceita = receitaRepository.save(receita);

        //Adiciona o log
        log.info("Receita salva com sucesso. ID: {}", novaReceita.getId());

        //Publica o evento para ser processado
        publisher.publishEvent(new ReceitaCriadaEvent(novaReceita.getId(), novaReceita.getNome()));

        return novaReceita;
    }

    public Receita updateReceita(ReceitaUpdateDTO receitaDto){
        Receita receita = receitaRepository.findById(receitaDto.id())
                .orElseThrow(ReceitaNotFoundException::new);

        receita.setDescricao(receitaDto.descricao());
        receita.setCusto(receitaDto.custo());

        Receita receitaAtualizada = receitaRepository.save(receita);

        //Adiciona o log
        log.info("Receita atualizada com sucesso. ID: {}", receitaAtualizada.getId());

        //Publica o evento para ser processado
        publisher.publishEvent(new ReceitaAtualizadaEvent(receitaAtualizada.getId(), receitaAtualizada.getNome()));

        return receitaAtualizada;
    }

    public Receita deleteReceita(int id){
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(ReceitaNotFoundException::new);

        receitaRepository.deleteById(id);

        return receita;
    }
}
