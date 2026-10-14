package com.hcLinear.backendTest.controllers;

import com.hcLinear.backendTest.dtos.TransferDocumentResponse;
import com.hcLinear.backendTest.services.TransferDocumentPdfService;
import com.hcLinear.backendTest.services.TransferDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer-documents")
@RequiredArgsConstructor
@Tag(name = "Transfer documents",
        description = "Sikeres átigazolásokat érvényesítő dokumentumok lekérdezése (pl. ATIG000014HU)")
public class TransferDocumentController {

    private final TransferDocumentService documentService;
    private final TransferDocumentPdfService pdfService;

    @GetMapping
    @Operation(summary = "Dokumentumok listázása játékos / forrás- / célcsapat / dokumentumszám szerint szűrve")
    public List<TransferDocumentResponse> list(
            @RequestParam(required = false) Long playerId,
            @RequestParam(required = false) Long sourceTeamId,
            @RequestParam(required = false) Long targetTeamId,
            @RequestParam(required = false) String documentNumber) {
        return documentService.list(playerId, sourceTeamId, targetTeamId, documentNumber);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Egy átigazolási dokumentum lekérdezése belső azonosító alapján")
    public TransferDocumentResponse load(@PathVariable Long id) {
        return documentService.load(id);
    }

    @GetMapping("/by-number/{documentNumber}")
    @Operation(summary = "Egy átigazolási dokumentum lekérdezése emberileg olvasható azonosító alapján "
            + "(pl. ATIG000014HU)")
    public TransferDocumentResponse loadByDocumentNumber(@PathVariable String documentNumber) {
        return documentService.loadByDocumentNumber(documentNumber);
    }

    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Átigazolási dokumentum nyomtatása PDF formátumban belső azonosító alapján")
    public ResponseEntity<byte[]> printById(@PathVariable Long id) {
        byte[] pdf = pdfService.generatePdfById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("atigazolas-" + id + ".pdf")
                .build());
        headers.setContentLength(pdf.length);
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping(value = "/by-number/{documentNumber}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Átigazolási dokumentum nyomtatása PDF formátumban dokumentumszám alapján "
            + "(pl. ATIG000014HU)")
    public ResponseEntity<byte[]> printByDocumentNumber(@PathVariable String documentNumber) {
        byte[] pdf = pdfService.generatePdfByDocumentNumber(documentNumber);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("atigazolas-" + documentNumber + ".pdf")
                .build());
        headers.setContentLength(pdf.length);
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
