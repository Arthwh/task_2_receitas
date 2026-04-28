package com.arthwh.registroReceitas.controller;

import com.arthwh.registroReceitas.dto.ReceitaRegisterDTO;
import com.arthwh.registroReceitas.dto.ReceitaUpdateDTO;
import com.arthwh.registroReceitas.model.Receita;
import com.arthwh.registroReceitas.model.TipoReceitaEnum;
import com.arthwh.registroReceitas.service.ReceitaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<Receita>> getReceitas(
            @RequestParam(required = false) TipoReceitaEnum tipoReceita,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio
    ){
        List<Receita> receitas = receitaService.getReceitasComFiltros(tipoReceita, dataInicio);
        return ResponseEntity.status(HttpStatus.OK).body(receitas);
    }

    @PostMapping
    public ResponseEntity<Receita> createReceita(@RequestBody ReceitaRegisterDTO receitaDto){
        if (receitaDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Receita receita = receitaService.createReceita(receitaDto);

        if (receita != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(receita);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping
    public ResponseEntity<Receita> updateReceita(@RequestBody ReceitaUpdateDTO receitaDto){
        if (receitaDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Receita receita = receitaService.updateReceita(receitaDto);
        if (receita != null){
            return ResponseEntity.status(HttpStatus.OK).body(receita);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Receita> deleteReceita(@PathVariable int id){
        Receita receita = receitaService.deleteReceita(id);
        if (receita != null){
            return ResponseEntity.status(HttpStatus.OK).body(receita);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

//    @GetMapping("/export")
//    public ResponseEntity<byte[]> exportReceitasToPdf(@RequestParam("tipo") String tipoReceita,
//                                                      @RequestParam("dataInicio") String  dataInicio,
//                                                      @RequestParam("dataFim") String dataFim){
//        List<Receita> receitas = receitaService.getReceitas();
//    }
}
