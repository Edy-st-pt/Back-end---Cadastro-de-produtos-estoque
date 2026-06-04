package com.back.cadastroeestoque.Mapper;

import com.back.cadastroeestoque.Dto.ProdutoRequestDTO;
import com.back.cadastroeestoque.Dto.ProdutoResponseDTO;
import com.back.cadastroeestoque.Model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setQuantidade(dto.quantidade());
        produto.setCategoria(dto.categoria());
        produto.setAtivo(dto.ativo() != null ? dto.ativo() : true);
        return produto;
    }

    public ProdutoResponseDTO toResponse(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getCategoria(),
                produto.getAtivo(),
                produto.getCriadoEm(),
                produto.getAtualizadoEm()
        );
    }

    public void updateFromDTO(ProdutoRequestDTO dto, Produto produto) {
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setQuantidade(dto.quantidade());
        produto.setCategoria(dto.categoria());
        if (dto.ativo() != null) {
            produto.setAtivo(dto.ativo());
        }
    }
}
