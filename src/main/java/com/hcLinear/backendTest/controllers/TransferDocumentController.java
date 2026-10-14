package com.hcLinear.backendTest.controllers;

import com.hcLinear.backendTest.dtos.TransferDocumentResponse;
import com.hcLinear.backendTest.services.TransferDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
}
