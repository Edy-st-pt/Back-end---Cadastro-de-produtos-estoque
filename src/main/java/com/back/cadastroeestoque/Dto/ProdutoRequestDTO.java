package com.back.cadastroeestoque.Dto;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        String nome,
        String descricao,
        BigDecimal preco,
        Integer quantidade,
        String categoria,
        Boolean ativo
) {}
