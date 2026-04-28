package com.arthwh.registroReceitas.export;

import com.arthwh.registroReceitas.model.Receita;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class PdfGenerator {

    public ByteArrayOutputStream generatePdfStream(List<Receita> receitas) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate()); // rotate() para modo paisagem, cabe mais colunas
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        // Título do PDF
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Relatório de Receitas", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20); // Espaço entre o título e a tabela
        document.add(title);

        // Cria uma tabela com 6 colunas (ID, Nome, Descrição, Data, Custo, Tipo)
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100); // Tabela ocupa 100% da largura da página

        // Define as larguras relativas das colunas
        try {
            table.setWidths(new float[]{1f, 3f, 4f, 2f, 2f, 2f});
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        // Cabeçalhos da Tabela
        addTableHeader(table, "ID");
        addTableHeader(table, "Nome");
        addTableHeader(table, "Descrição");
        addTableHeader(table, "Data Registro");
        addTableHeader(table, "Custo");
        addTableHeader(table, "Tipo");

        // Formatador de data
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // Preenchendo as linhas com os dados das receitas
        for (Receita receita : receitas) {
            table.addCell(String.valueOf(receita.getId()));
            table.addCell(receita.getNome() != null ? receita.getNome() : "");
            table.addCell(receita.getDescricao() != null ? receita.getDescricao() : "");

            // Formata a data se não for nula
            String dataStr = (receita.getDataRegistro() != null) ? sdf.format(receita.getDataRegistro()) : "";
            table.addCell(dataStr);

            // Formata o custo (ex: R$ 15.50)
            table.addCell(String.format("R$ %.2f", receita.getCusto()));

            // Pega o nome do Enum
            table.addCell(receita.getTipoReceita() != null ? receita.getTipoReceita().name() : "");
        }

        // Adiciona a tabela ao documento
        document.add(table);
        document.close();

        return outputStream;
    }

    // Método auxiliar para criar os cabeçalhos com estilo
    private void addTableHeader(PdfPTable table, String headerTitle) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(1);
        header.setPhrase(new Phrase(headerTitle, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setPadding(5);
        table.addCell(header);
    }
}
