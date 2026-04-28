package com.arthwh.registroReceitas.controller;

import com.arthwh.registroReceitas.dto.ReceitaRegisterDTO;
import com.arthwh.registroReceitas.dto.ReceitaUpdateDTO;
import com.arthwh.registroReceitas.model.Receita;
import com.arthwh.registroReceitas.service.ReceitaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {
    private final ReceitaService receitaService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receita> getReceitaById(@PathVariable int id){
        Receita receita = receitaService.getReceitaById(id);

        if  (receita != null){
            return ResponseEntity.status(HttpStatus.OK).body(receita);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping
    public ResponseEntity<List<Receita>> getReceitas(){
        List<Receita> receitas = receitaService.getReceitas();
        if (receitas != null){
            return ResponseEntity.status(HttpStatus.OK).body(receitas);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    public ResponseEntity<Receita> createReceita(@RequestBody ReceitaRegisterDTO receitaDto){
        if (receitaDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Receita receita = receitaService.createReceita(receitaDto);

        if (receita != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(receita);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PutMapping
    public ResponseEntity<Receita> updateReceita(@RequestBody ReceitaUpdateDTO receitaDto){
        if (receitaDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Receita receita = receitaService.updateReceita(receitaDto);
        if (receita != null){
            return ResponseEntity.status(HttpStatus.OK).body(receita);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Receita> deleteReceita(@PathVariable int id){
        Receita receita = receitaService.deleteReceita(id);
        if (receita != null){
            return ResponseEntity.status(HttpStatus.OK).body(receita);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
