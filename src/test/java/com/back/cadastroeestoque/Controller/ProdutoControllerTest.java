package com.back.cadastroeestoque.Controller;

import com.back.cadastroeestoque.Dto.ProdutoRequestDTO;
import com.back.cadastroeestoque.Dto.ProdutoResponseDTO;
import com.back.cadastroeestoque.Exception.GlobalExceptionHandler;
import com.back.cadastroeestoque.Exception.ProdutoNotFoundException;
import com.back.cadastroeestoque.Service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    private ProdutoRequestDTO requestDTO;
    private ProdutoResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(produtoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

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
    void deveListarTodosOsProdutos() throws Exception {
        when(produtoService.listarTodos()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Notebook"))
                .andExpect(jsonPath("$[0].preco").value(4500.00));
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {
        when(produtoService.buscarPorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Notebook"));
    }

    @Test
    void deveRetornar404QuandoProdutoNaoEncontrado() throws Exception {
        when(produtoService.buscarPorId(99L)).thenThrow(new ProdutoNotFoundException(99L));

        mockMvc.perform(get("/api/produtos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Produto não encontrado com id: 99"));
    }

    @Test
    void deveCadastrarProdutoComSucesso() throws Exception {
        when(produtoService.cadastrar(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Notebook"));
    }

    @Test
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        ProdutoRequestDTO dtoInvalido = new ProdutoRequestDTO(
                "",
                null,
                new BigDecimal("-1"),
                -1,
                "",
                null
        );

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAtualizarProdutoComSucesso() throws Exception {
        when(produtoService.atualizar(eq(1L), any())).thenReturn(responseDTO);

        mockMvc.perform(put("/api/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Notebook"));
    }

    @Test
    void deveDeletarProdutoComSucesso() throws Exception {
        doNothing().when(produtoService).deletar(1L);

        mockMvc.perform(delete("/api/produtos/1"))
                .andExpect(status().isNoContent());
    }
}