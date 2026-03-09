package com.example.java_project.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class ALunoController {
    private final AlunoService alunoService;

    @PostMapping
    public ResponseEntity<AlunoResponseDto> cadastrarUsuario(@RequestBody @Valid AlunoDto dto) {
        AlunoResponseDto response = alunoService.cadastrarUsuario(dto);
        return ResponseEntity.ok(response);
    }
