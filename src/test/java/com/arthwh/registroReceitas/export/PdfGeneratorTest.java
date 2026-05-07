package com.arthwh.registroReceitas.export;

import com.arthwh.registroReceitas.model.Receita;
import com.arthwh.registroReceitas.model.TipoReceitaEnum;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class PdfGeneratorTest {
    private PdfGenerator pdfGenerator;

    @BeforeEach
    void setUp() {
        pdfGenerator = new PdfGenerator();
    }

    @Test
    @DisplayName("Case 1: Should generate the pdf file successfully with the recipe data that was sent to the method.")
    void generatePdfStream_ComSucesso() throws Exception {
        //Cria os objetos que serão passados para o método
        Receita receita = criarReceita();
        List<Receita> receitas = List.of(receita);

        ByteArrayOutputStream outputStream = pdfGenerator.generatePdfStream(receitas);

        //Verifica se o arquivo gerado não está vazio
        assertNotNull(outputStream, "O stream de saída não deveria ser nulo");
        assertTrue(outputStream.toByteArray().length > 0, "O arquivo PDF gerado não deveria estar vazio");

        // Usa o PdfReader para ler os bytes gerados
        PdfReader leitorPdf = new PdfReader(outputStream.toByteArray());

        // Extrai o texto da primeira página do PDF
        String texto = PdfTextExtractor.getTextFromPage(leitorPdf, 1);

        // Verifica se o título e os dados da receita realmente foram escritos no PDF
        assertTrue(texto.contains("Relatório de Receitas"), "Deveria conter o título do relatório");
        assertTrue(texto.contains(receita.getNome()), "Deveria conter o nome da receita");
        assertTrue(texto.contains(receita.getDescricao()), "Deveria conter a descrição");
        assertTrue(texto.contains(receita.getTipoReceita().name()), "Deveria conter o tipo da receita");

        // Fecha o leitor
        leitorPdf.close();
    }

    @Test
    @DisplayName("Case 2: Should generate the pdf file with just the header and an empty list.")
    void generatePdfStreamSuccess_EmptyList() throws Exception {
        List<Receita> listaVazia = List.of();

        ByteArrayOutputStream outputStream = pdfGenerator.generatePdfStream(listaVazia);

        //Garante que o arquivo não está vazio
        assertNotNull(outputStream);

        //Extrai o conteúdo do arquivo para garantir validar o conteúdo
        PdfReader leitorPdf = new PdfReader(outputStream.toByteArray());
        String texto = PdfTextExtractor.getTextFromPage(leitorPdf, 1);

        // Verifica se os cabeçalhos foram gerados, mas sem dados
        assertTrue(texto.contains("Relatório de Receitas"));
        assertTrue(texto.contains("ID"));
        assertTrue(texto.contains("Nome"));
        assertTrue(texto.contains("Descrição"));
        assertTrue(texto.contains("Data Registro"));
        assertTrue(texto.contains("Custo"));
        assertTrue(texto.contains("Tipo"));

        leitorPdf.close();
    }

    @Test
    @DisplayName("Case 3: Should generate the pdf file successfylly and don't throw an NullPointerException.")
    void generatePdfStream_ComReceitaComCamposNulos() throws Exception {
        //Cria a receita sem completar todos os campos
        //Nome, Descrição, Data e TipoReceita ficam NULL
        Receita receitaIncompleta = new Receita();
        receitaIncompleta.setId(1);
        receitaIncompleta.setCusto(0.0);

        List<Receita> receitas = List.of(receitaIncompleta);

        // Usa assertDoesNotThrow para provar que não lançará uma excessão
        ByteArrayOutputStream outputStream = assertDoesNotThrow(() ->
                pdfGenerator.generatePdfStream(receitas)
        );

        //Extrai o conteúdo do arquivo para garantir validar o conteúdo
        PdfReader leitorPdf = new PdfReader(outputStream.toByteArray());
        String texto = PdfTextExtractor.getTextFromPage(leitorPdf, 1);

        assertTrue(texto.contains("1"));
        assertTrue(texto.contains("R$ 0,00") || texto.contains("R$ 0.00"));

        leitorPdf.close();
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