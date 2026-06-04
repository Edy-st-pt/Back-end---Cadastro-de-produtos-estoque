# Nota Técnica

## Decisões técnicas

### Arquitetura em camadas
O projeto segue o padrão de arquitetura em camadas (Controller → Service → Repository), separando as responsabilidades de cada parte da aplicação e facilitando a manutenção e os testes.

### DTOs e Mapper manual
Optei por usar Records do Java para os DTOs por serem imutáveis e eliminarem boilerplate. A conversão entre entidade e DTO foi feita de forma manual em uma classe `ProdutoMapper`, evitando dependências externas e tornando o código mais transparente.

### Exceções específicas
Em vez de lançar `RuntimeException` genérico, foram criadas exceções específicas (`ProdutoNotFoundException`, `ProdutoDuplicadoException`) tratadas pelo `GlobalExceptionHandler`, garantindo respostas padronizadas e com os status HTTP corretos.

### Flyway para migrations
O Flyway foi utilizado para o versionamento do banco de dados, garantindo que as alterações no schema sejam rastreáveis e reproduzíveis em qualquer ambiente.

### Logs estruturados
Foram adicionados logs em todos os pontos relevantes da camada de serviço usando SLF4J, diferenciando níveis de `info` para operações normais e `warn`/`error` para situações de falha, facilitando o diagnóstico em produção.

### Validações
As validações dos dados de entrada foram feitas com Bean Validation (`@NotBlank`, `@Min`, `@DecimalMin`) diretamente nos DTOs, mantendo as regras próximas aos dados e evitando que dados inválidos cheguem à camada de serviço.

## Trade-offs

- **`ddl-auto=update`** foi utilizado para facilitar o desenvolvimento local. Em produção o ideal seria `validate`, delegando toda a gestão do schema ao Flyway.
- **Sem autenticação**: a API não possui segurança implementada. Em um cenário real seria necessário adicionar Spring Security com JWT.
- **`@CrossOrigin(origins = "*")`**: liberado para qualquer origem para facilitar a integração com o front-end. Em produção deve ser restrito ao domínio da aplicação.

## Possíveis melhorias futuras

- Adicionar autenticação e autorização com Spring Security e JWT
- Implementar paginação na listagem de produtos
- Adicionar filtros por categoria, nome e status na listagem
- Criar endpoint de relatório de estoque baixo
- Configurar variáveis de ambiente via `.env` ou secrets
- Adicionar Docker para facilitar a execução em qualquer ambiente
- Implementar cache com Redis para a listagem de produtos
