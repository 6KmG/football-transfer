package com.hcLinear.backendTest.controllers;

import com.hcLinear.backendTest.dtos.TransferCreateRequest;
import com.hcLinear.backendTest.dtos.TransferResponse;
import com.hcLinear.backendTest.enums.TransferStatus;
import com.hcLinear.backendTest.services.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@Tag(name = "Transfers", description = "Átigazolási kérelmek kezelése (létrehozás, listázás, elfogadás, elutasítás)")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Új átigazolási kérelem létrehozása")
    public TransferResponse create(@Valid @RequestBody TransferCreateRequest request) {
        return transferService.create(request);
    }

    @GetMapping
    @Operation(summary = "Kérelmek listázása játékos / forrás- / célcsapat / státusz szerint szűrve")
    public List<TransferResponse> list(
            @RequestParam(required = false) Long playerId,
            @RequestParam(required = false) Long sourceTeamId,
            @RequestParam(required = false) Long targetTeamId,
            @RequestParam(required = false) TransferStatus status) {
        return transferService.list(playerId, sourceTeamId, targetTeamId, status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Egy átigazolási kérelem lekérdezése azonosító alapján")
    public TransferResponse load(@PathVariable Long id) {
        return transferService.load(id);
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "Átigazolási kérelem elfogadása és végrehajtása")
    public TransferResponse accept(@PathVariable Long id) {
        return transferService.accept(id);
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Átigazolási kérelem elutasítása")
    public TransferResponse reject(@PathVariable Long id) {
        return transferService.reject(id);
    }
}
