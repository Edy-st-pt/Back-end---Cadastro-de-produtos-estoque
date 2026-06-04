package com.back.cadastroeestoque.Exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponse(
        int status,
        String mensagem,
        List<String> erros,
        LocalDateTime timestamp
) {}
