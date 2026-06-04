package com.back.cadastroeestoque.Service;

import com.back.cadastroeestoque.Dto.ProdutoRequestDTO;
import com.back.cadastroeestoque.Dto.ProdutoResponseDTO;
import com.back.cadastroeestoque.Exception.ProdutoDuplicadoException;
import com.back.cadastroeestoque.Exception.ProdutoNotFoundException;
import com.back.cadastroeestoque.Mapper.ProdutoMapper;
import com.back.cadastroeestoque.Model.Produto;
import com.back.cadastroeestoque.Repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        log.info("Buscando todos os produtos");
        return produtoRepository.findAll()
                .stream()
                .map(produtoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        log.info("Buscando produto com id: {}", id);
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException(id));
        return produtoMapper.toResponse(produto);
    }

    @Transactional
    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto) {
        log.info("Cadastrando novo produto: {}", dto.nome());

        if (produtoRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new ProdutoDuplicadoException(dto.nome());
        }

        Produto produto = produtoMapper.toEntity(dto);
        Produto salvo = produtoRepository.save(produto);
        log.info("Produto cadastrado com sucesso. ID: {}", salvo.getId());
        return produtoMapper.toResponse(salvo);
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        log.info("Atualizando produto com id: {}", id);

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException(id));

        produtoMapper.updateFromDTO(dto, produto);
        Produto atualizado = produtoRepository.save(produto);
        log.info("Produto atualizado com sucesso. ID: {}", atualizado.getId());
        return produtoMapper.toResponse(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Removendo produto com id: {}", id);

        if (!produtoRepository.existsById(id)) {
            throw new ProdutoNotFoundException(id);
        }

        produtoRepository.deleteById(id);
        log.info("Produto removido com sucesso. ID: {}", id);
    }
}
