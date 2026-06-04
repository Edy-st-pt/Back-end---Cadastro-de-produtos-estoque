package com.back.cadastroeestoque.Dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProdutoRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String descricao,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal preco,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 0, message = "A quantidade não pode ser negativa")
        Integer quantidade,

        @NotBlank(message = "A categoria é obrigatória")
        @Size(max = 50, message = "A categoria deve ter no máximo 50 caracteres")
        String categoria,

        Boolean ativo

) {}
