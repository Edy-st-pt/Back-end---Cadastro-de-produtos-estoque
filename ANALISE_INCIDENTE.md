# Análise de Incidente

## Descrição do cenário

Durante o monitoramento da aplicação em produção, foi identificado um erro recorrente na rota `POST /api/produtos`. Em horários de maior uso, a API começou a retornar o status `500 Internal Server Error` de forma intermitente, impactando o cadastro de novos produtos.

## Logs identificados

```
2025-06-04 14:22:31 INFO  ProdutoService - Cadastrando novo produto: Notebook Gamer
2025-06-04 14:22:31 ERROR GlobalExceptionHandler - Erro inesperado: could not execute statement [ERROR: duplicate key value violates unique constraint "produtos_nome_key"]
org.springframework.dao.DataIntegrityViolationException: could not execute statement
    at ProdutoService.cadastrar(ProdutoService.java:48)
    at ProdutoController.cadastrar(ProdutoController.java:38)

2025-06-04 14:22:45 INFO  ProdutoService - Cadastrando novo produto: Mouse Wireless
2025-06-04 14:22:45 ERROR GlobalExceptionHandler - Erro inesperado: could not execute statement [ERROR: duplicate key value violates unique constraint "produtos_nome_key"]
org.springframework.dao.DataIntegrityViolationException: could not execute statement
    at ProdutoService.cadastrar(ProdutoService.java:48)
    at ProdutoController.cadastrar(ProdutoController.java:38)

2025-06-04 14:23:10 WARN  ProdutoService - Já existe um produto cadastrado com o nome: Teclado Mecânico
2025-06-04 14:23:10 INFO  ProdutoService - Produto cadastrado com sucesso. ID: 87
```

## Diagnóstico

Analisando os logs, foram identificados dois comportamentos distintos:

**Erro 1 — Condição de corrida (Race Condition):** Dois usuários tentaram cadastrar o produto com o mesmo nome ao mesmo tempo. A verificação `existsByNomeIgnoreCase` passou para ambas as requisições antes de qualquer uma concluir o `save`, permitindo que as duas tentassem inserir no banco. O banco rejeitou a segunda inserção com erro de chave duplicada, mas a aplicação não tratou esse caso, retornando 500 ao invés de 409.

**Comportamento correto (linha 3):** Quando apenas uma requisição tentou cadastrar, a verificação funcionou normalmente e o produto foi salvo com sucesso.

## Causa raiz

A verificação de duplicidade no `ProdutoService` não é atômica — existe uma janela de tempo entre o `existsByNomeIgnoreCase` e o `save` onde outra requisição pode passar pela mesma verificação. Isso causa a `DataIntegrityViolationException` que não estava sendo tratada pelo `GlobalExceptionHandler`.

## Correções aplicadas

### 1. Tratar a exceção de violação de integridade no GlobalExceptionHandler

```java
@ExceptionHandler(DataIntegrityViolationException.class)
public ResponseEntity<ErroResponse> handleIntegridade(DataIntegrityViolationException ex) {
    log.warn("Violação de integridade no banco de dados: {}", ex.getMessage());
    ErroResponse erro = new ErroResponse(
            HttpStatus.CONFLICT.value(),
            "Já existe um produto cadastrado com esse nome",
            List.of(),
            LocalDateTime.now()
    );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
}
```

### 2. Adicionar constraint de unicidade no banco via migration

```sql
-- V2__add_unique_constraint_nome.sql
ALTER TABLE produtos ADD CONSTRAINT produtos_nome_unique UNIQUE (nome);
```

Dessa forma, mesmo que a verificação em memória falhe, o banco garante a integridade dos dados e a exceção é tratada corretamente.

## Medidas de prevenção

- **Tratar `DataIntegrityViolationException`** no handler global para que erros de banco nunca retornem 500 sem tratamento adequado
- **Adicionar constraints no banco** como segunda linha de defesa, independente da validação na camada de serviço
- **Monitoramento de erros 500** com alertas automáticos para identificar padrões recorrentes rapidamente
- **Testes de carga** para simular requisições simultâneas e identificar condições de corrida antes de ir para produção
- **Logs mais detalhados** incluindo o nome do usuário ou IP da requisição para facilitar a correlação de eventos durante a investigação
