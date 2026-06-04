package com.back.cadastroeestoque.Exception;

public class ProdutoNotFoundException extends RuntimeException {

    public ProdutoNotFoundException(Long id) {
        super("Produto não encontrado com id: " + id);
    }
}
