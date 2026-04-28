package com.arthwh.registroReceitas.service;

import com.arthwh.registroReceitas.export.PdfGenerator;
import com.arthwh.registroReceitas.model.Receita;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExportService {
    private final PdfGenerator pdfGenerator;

    public ExportService(PdfGenerator pdfGenerator) {
        this.pdfGenerator = pdfGenerator;
    }

    public ByteArrayOutputStream exportarReceitasParaPdf(List<Receita> receitas) {
        try {
            // Chama o gerador de PDF passando a lista de receitas
            return pdfGenerator.generatePdfStream(receitas);
        } catch (DocumentException e) {
            // Transforma a exceção do iText em uma exceção de runtime do Java
            throw new RuntimeException("Erro ao gerar o PDF das receitas", e);
        }
    }
}
