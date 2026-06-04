package com.back.cadastroeestoque.Service;

import com.back.cadastroeestoque.Dto.ProdutoRequestDTO;
import com.back.cadastroeestoque.Dto.ProdutoResponseDTO;
import com.back.cadastroeestoque.Exception.ProdutoDuplicadoException;
import com.back.cadastroeestoque.Exception.ProdutoNotFoundException;
import com.back.cadastroeestoque.Mapper.ProdutoMapper;
import com.back.cadastroeestoque.Model.Produto;
import com.back.cadastroeestoque.Repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private ProdutoRequestDTO requestDTO;
    private ProdutoResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Notebook");
        produto.setDescricao("Notebook gamer");
        produto.setPreco(new BigDecimal("4500.00"));
        produto.setQuantidade(10);
        produto.setCategoria("Eletrônicos");
        produto.setAtivo(true);

        requestDTO = new ProdutoRequestDTO(
                "Notebook",
                "Notebook gamer",
                new BigDecimal("4500.00"),
                10,
                "Eletrônicos",
                true
        );

        responseDTO = new ProdutoResponseDTO(
                1L,
                "Notebook",
                "Notebook gamer",
                new BigDecimal("4500.00"),
                10,
                "Eletrônicos",
                true,
                null,
                null
        );
    }

    @Test
    void deveListarTodosOsProdutos() {
        when(produtoRepository.findAll()).thenReturn(List.of(produto));
        when(produtoMapper.toResponse(produto)).thenReturn(responseDTO);

        List<ProdutoResponseDTO> resultado = produtoService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Notebook", resultado.get(0).nome());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarProdutoPorId() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoMapper.toResponse(produto)).thenReturn(responseDTO);

        ProdutoResponseDTO resultado = produtoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Notebook", resultado.nome());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProdutoNotFoundException.class, () -> produtoService.buscarPorId(99L));
    }

    @Test
    void deveCadastrarProdutoComSucesso() {
        when(produtoRepository.existsByNomeIgnoreCase("Notebook")).thenReturn(false);
        when(produtoMapper.toEntity(requestDTO)).thenReturn(produto);
        when(produtoRepository.save(produto)).thenReturn(produto);
        when(produtoMapper.toResponse(produto)).thenReturn(responseDTO);

        ProdutoResponseDTO resultado = produtoService.cadastrar(requestDTO);

        assertNotNull(resultado);
        assertEquals("Notebook", resultado.nome());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void deveLancarExcecaoAoCadastrarProdutoDuplicado() {
        when(produtoRepository.existsByNomeIgnoreCase("Notebook")).thenReturn(true);

        assertThrows(ProdutoDuplicadoException.class, () -> produtoService.cadastrar(requestDTO));
        verify(produtoRepository, never()).save(any());
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(produto)).thenReturn(produto);
        when(produtoMapper.toResponse(produto)).thenReturn(responseDTO);

        ProdutoResponseDTO resultado = produtoService.atualizar(1L, requestDTO);

        assertNotNull(resultado);
        verify(produtoMapper, times(1)).updateFromDTO(requestDTO, produto);
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void deveLancarExcecaoAoAtualizarProdutoInexistente() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProdutoNotFoundException.class, () -> produtoService.atualizar(99L, requestDTO));
    }

    @Test
    void deveDeletarProdutoComSucesso() {
        when(produtoRepository.existsById(1L)).thenReturn(true);

        produtoService.deletar(1L);

        verify(produtoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoDeletarProdutoInexistente() {
        when(produtoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProdutoNotFoundException.class, () -> produtoService.deletar(99L));
        verify(produtoRepository, never()).deleteById(any());
    }
}
