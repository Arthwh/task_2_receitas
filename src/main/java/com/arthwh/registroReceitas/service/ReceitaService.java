package com.arthwh.registroReceitas.service;

import com.arthwh.registroReceitas.dto.ReceitaCreateDTO;
import com.arthwh.registroReceitas.dto.ReceitaUpdateDTO;
import com.arthwh.registroReceitas.event.ReceitaAtualizadaEvent;
import com.arthwh.registroReceitas.event.ReceitaCriadaEvent;
import com.arthwh.registroReceitas.model.Receita;
import com.arthwh.registroReceitas.repository.ReceitaRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReceitaService {
    private final ReceitaRepository receitaRepository;
    private final ApplicationEventPublisher publisher;

    public ReceitaService(ReceitaRepository receitaRepository, ApplicationEventPublisher publisher) {
        this.receitaRepository = receitaRepository;
        this.publisher = publisher;
    }

    public Receita getReceitaById(int id){
        return receitaRepository.getReferenceById(id);
    }

    public List<Receita> getReceitas(){
        return  receitaRepository.findAll();
    }

    public Receita createReceita(ReceitaCreateDTO receitaDto){
        Receita receita = new Receita();

        receita.setNome(receitaDto.nome());
        receita.setDescricao(receitaDto.descricao());
        receita.setCusto(receitaDto.custo());
        receita.setTipoReceita(receitaDto.tipoReceita());

        Receita novaReceita = receitaRepository.save(receita);

        //Publica o evento para ser processado
        publisher.publishEvent(new ReceitaCriadaEvent(novaReceita.getId(), novaReceita.getNome()));

        return novaReceita;
    }

    public Receita updateReceita(ReceitaUpdateDTO receitaDto){
        Receita receita = receitaRepository.getReferenceById(receitaDto.id());

        if (receita == null){
            throw new NoSuchElementException("Receita não encontrada!");
        }

        receita.setDescricao(receitaDto.descricao());
        receita.setCusto(receitaDto.custo());

        Receita receitaAtualizada = receitaRepository.save(receita);

        //Publica o evento para ser processado
        publisher.publishEvent(new ReceitaAtualizadaEvent(receitaAtualizada.getId(), receitaAtualizada.getNome()));

        return receitaAtualizada;
    }

    public Receita deleteReceita(int id){
        Receita receita =  receitaRepository.getReferenceById(id);
        if (receita == null){
            return null;
        }

        receitaRepository.deleteById(id);

        return receita;
    }
}
