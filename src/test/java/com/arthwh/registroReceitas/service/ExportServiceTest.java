package com.arthwh.registroReceitas.service;

import com.arthwh.registroReceitas.export.PdfGenerator;
import com.arthwh.registroReceitas.model.Receita;
import com.arthwh.registroReceitas.model.TipoReceitaEnum;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportServiceTest {
    @Mock
    PdfGenerator pdfGenerator;

    ExportService exportService;

    @BeforeEach
    void setUp() {
        exportService = new ExportService(pdfGenerator);
    }

    @Test
    @DisplayName("Case 1: Should export to pdf successfully.")
    void exportarReceitasParaPdfSuccess() throws DocumentException {
        List<Receita> listaDeReceitas = List.of(criarReceita());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); //Cria um output vazio

        when(pdfGenerator.generatePdfStream(listaDeReceitas)).thenReturn(outputStream);

        ByteArrayOutputStream resultado = exportService.exportarReceitasParaPdf(listaDeReceitas);

        assertNotNull(resultado);
        assertEquals(outputStream, resultado);

        verify(pdfGenerator, times(1)).generatePdfStream(listaDeReceitas);
    }

    @Test
    @DisplayName("Case 2: Should not export to pdf.")
    void exportarReceitasParaPdfError() throws DocumentException {
        List<Receita> listaDeReceitas = List.of(criarReceita());

        when(pdfGenerator.generatePdfStream(listaDeReceitas))
                .thenThrow(new DocumentException("Erro de criação do documento pdf."));

        // Verifica se foi capturada a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            exportService.exportarReceitasParaPdf(listaDeReceitas);
        });

        // Verifica se a causa raiz da exceção é a DocumentException original
        assertInstanceOf(DocumentException.class, exception.getCause());
    }

    private Receita criarReceita() {
        Receita receita = new Receita();
        receita.setId(1);
        receita.setNome("Bolo de cenoura");
        receita.setDescricao("Bolo de cenoura fofinho.");
        receita.setDataRegistro(new Timestamp(System.currentTimeMillis()));
        receita.setCusto(30.00);
        receita.setTipoReceita(TipoReceitaEnum.DOCE);

        return receita;
    }
}