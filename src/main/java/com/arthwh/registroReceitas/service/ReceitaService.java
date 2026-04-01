package com.arthwh.registroReceitas.service;

import com.arthwh.registroReceitas.dto.ReceitaCreateDTO;
import com.arthwh.registroReceitas.dto.ReceitaUpdateDTO;
import com.arthwh.registroReceitas.model.Receita;
import com.arthwh.registroReceitas.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReceitaService {
    @Autowired
    private ReceitaRepository receitaRepository;

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

        return receitaRepository.save(receita);
    }

    public Receita updateReceita(ReceitaUpdateDTO receitaDto){
        Receita receita = receitaRepository.getReferenceById(receitaDto.id());

        if (receita == null){
            throw new NoSuchElementException("Receita não encontrada!");
        }

        receita.setDescricao(receitaDto.descricao());
        receita.setCusto(receitaDto.custo());

        return receitaRepository.save(receita);
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
