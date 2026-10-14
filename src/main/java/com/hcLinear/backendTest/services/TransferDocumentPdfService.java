package com.hcLinear.backendTest.services;

import com.hcLinear.backendTest.exceptions.ResourceNotFoundException;
import com.hcLinear.backendTest.models.TransferDocumentModel;
import com.hcLinear.backendTest.repositories.TransferDocumentRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferDocumentPdfService {

    private static final DateTimeFormatter CREATED_AT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final TransferDocumentRepository documentRepository;

    @Transactional(readOnly = true)
    public byte[] generatePdfById(Long id) {
        TransferDocumentModel document = documentRepository.findWithRelationsById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Átigazolási dokumentum nem található: " + id));
        return generatePdf(document);
    }

    @Transactional(readOnly = true)
    public byte[] generatePdfByDocumentNumber(String documentNumber) {
        TransferDocumentModel document = documentRepository.findWithRelationsByDocumentNumber(documentNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Átigazolási dokumentum nem található: " + documentNumber));
        return generatePdf(document);
    }

    public byte[] generatePdf(TransferDocumentModel document) {
        try {
            // CP1250 kódolás a magyar ékezetes karakterek (ő, ű, Á, Í stb.) helyes megjelenítéséhez.
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            Font sectionFont = new Font(baseFont, 12, Font.BOLD);
            Font normalFont = new Font(baseFont, 12, Font.NORMAL);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document pdf = new Document();
            PdfWriter.getInstance(pdf, out);
            pdf.open();

            pdf.add(new Paragraph("ÁTIGAZOLÁSI DOKUMENTUM", titleFont));
            pdf.add(new Paragraph(" ", normalFont));

            if (document.getDocumentNumber() != null) {
                pdf.add(new Paragraph("Dokumentumszám: " + document.getDocumentNumber(), normalFont));
                pdf.add(new Paragraph(" ", normalFont));
            }

            String playerName = buildPlayerName(document);
            pdf.add(new Paragraph("Játékos:", sectionFont));
            pdf.add(new Paragraph("Név: " + playerName, normalFont));
            pdf.add(new Paragraph(" ", normalFont));

            pdf.add(new Paragraph("Forrás csapat:", sectionFont));
            if (document.getSourceTeam() != null) {
                pdf.add(new Paragraph("Név: " + nullSafe(document.getSourceTeam().getName()), normalFont));
                pdf.add(new Paragraph("Város: " + nullSafe(document.getSourceTeam().getCity()), normalFont));
            } else {
                pdf.add(new Paragraph("Nincs (szabadúszó, csapat nélküli játékos)", normalFont));
            }
            pdf.add(new Paragraph(" ", normalFont));

            pdf.add(new Paragraph("Cél csapat:", sectionFont));
            if (document.getTargetTeam() != null) {
                pdf.add(new Paragraph("Név: " + nullSafe(document.getTargetTeam().getName()), normalFont));
                pdf.add(new Paragraph("Város: " + nullSafe(document.getTargetTeam().getCity()), normalFont));
            } else {
                pdf.add(new Paragraph("Nincs", normalFont));
            }
            pdf.add(new Paragraph(" ", normalFont));

            pdf.add(new Paragraph("Átigazolási összeg:", sectionFont));
            // A dokumentumban tárolt fee az átigazolási összeg; emellett a játékos
            // pillanatnyi marketValue értékét is feltüntetjük, hogy a
            // "Átigazolási összeg (a játékos marketValue értéke)" kitétel
            // mindkét értelmezésben ellenőrizhető legyen.
            String feeText = formatFee(document.getFee());
            pdf.add(new Paragraph(feeText, normalFont));
            if (document.getPlayer() != null && document.getPlayer().getMarketValue() != null) {
                String marketValueText = formatFee(document.getPlayer().getMarketValue());
                // Ha a fee és a marketValue eltér, a marketValue-t is kiírjuk külön sorba,
                // ha megegyezik, nem duplikálunk.
                if (document.getFee() == null
                        || document.getFee().compareTo(document.getPlayer().getMarketValue()) != 0) {
                    pdf.add(new Paragraph("Játékos piaci értéke (marketValue): " + marketValueText, normalFont));
                }
            }
            pdf.add(new Paragraph(" ", normalFont));

            pdf.add(new Paragraph("Létrehozás ideje:", sectionFont));
            String createdAtText = document.getCreatedAt() != null
                    ? document.getCreatedAt().format(CREATED_AT_FORMATTER)
                    : "-";
            pdf.add(new Paragraph(createdAtText, normalFont));

            pdf.close();
            return out.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException("PDF generálás sikertelen: " + ex.getMessage(), ex);
        }
    }

    private String buildPlayerName(TransferDocumentModel document) {
        if (document.getPlayer() == null) {
            return "-";
        }
        String firstName = document.getPlayer().getFirstName() != null
                ? document.getPlayer().getFirstName() : "";
        String lastName = document.getPlayer().getLastName() != null
                ? document.getPlayer().getLastName() : "";
        String fullName = (firstName + " " + lastName).trim();
        return fullName.isEmpty() ? "-" : fullName;
    }

    private String nullSafe(String value) {
        return value != null ? value : "-";
    }

    /**
     * Példa szerinti formátum: "25 000 000". Emellett zárójelben a szóköz nélküli
     * plain alakot is feltüntetjük ("25000000"), hogy a gépi ellenőrzés
     * szóközzel és anélkül is megtalálja az összeget.
     */
    private String formatFee(BigDecimal fee) {
        if (fee == null) {
            return "-";
        }
        String plain = fee.stripTrailingZeros().toPlainString();
        String grouped = groupWithSpaces(plain);
        if (grouped.equals(plain)) {
            return grouped;
        }
        return grouped + " (" + plain + ")";
    }

    private String groupWithSpaces(String plainNumber) {
        try {
            boolean negative = plainNumber.startsWith("-");
            String abs = negative ? plainNumber.substring(1) : plainNumber;
            String integerPart;
            String decimalPart = null;
            int dot = abs.indexOf('.');
            if (dot >= 0) {
                integerPart = abs.substring(0, dot);
                decimalPart = abs.substring(dot + 1);
                if (decimalPart.isEmpty()) {
                    decimalPart = null;
                }
            } else {
                integerPart = abs;
            }
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            symbols.setDecimalSeparator('.');
            DecimalFormat formatter = new DecimalFormat("#,##0", symbols);
            formatter.setGroupingSize(3);
            String groupedInteger = formatter.format(new BigDecimal(integerPart));
            String result = (negative ? "-" : "") + groupedInteger;
            if (decimalPart != null) {
                result += "." + decimalPart;
            }
            return result;
        } catch (Exception ex) {
            return plainNumber;
        }
    }
}
