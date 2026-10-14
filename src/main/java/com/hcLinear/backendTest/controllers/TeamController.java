package com.hcLinear.backendTest.controllers;

import com.hcLinear.backendTest.dtos.TeamCreateRequest;
import com.hcLinear.backendTest.dtos.TeamResponse;
import com.hcLinear.backendTest.services.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Teams", description = "Csapatok kezelése (Create, List, Load, Delete)")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Új csapat létrehozása")
    public TeamResponse create(@Valid @RequestBody TeamCreateRequest request) {
        return teamService.create(request);
    }

    @GetMapping
    @Operation(summary = "Csapatok listázása (játékosszám + kapitánynév)")
    public List<TeamResponse> list() {
        return teamService.list();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Egy csapat lekérdezése azonosító alapján")
    public TeamResponse load(@PathVariable Long id) {
        return teamService.load(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Csapat törlése (játékosok megmaradnak, team_id SET NULL)")
    public void delete(@PathVariable Long id) {
        teamService.delete(id);
    }
}
