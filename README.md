# Cadastro e Estoque de Produtos

API REST para gerenciamento de produtos e controle de estoque, desenvolvida com Spring Boot e PostgreSQL.

## Tecnologias utilizadas

- Java 25
- Spring Boot 4.0.6
- Spring Data JPA
- Spring Validation
- PostgreSQL
- Flyway
- Lombok
- Swagger/OpenAPI (SpringDoc)
- JUnit 5 + Mockito

## Pré-requisitos

- Java 25+
- Maven 3.8+
- PostgreSQL instalado e rodando

## Como executar

### 1. Clonar o repositório

```bash
git clone https://github.com/Edy-st-pt/Back-end---Cadastro-de-produtos-estoque.git
cd Back-end---Cadastro-de-produtos-estoque
```

### 2. Criar o banco de dados

Acesse o PostgreSQL e execute:

```sql
CREATE DATABASE estoque_db;
```

### 3. Configurar as credenciais

No arquivo `src/main/resources/application.properties`, ajuste as credenciais se necessário:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/estoque_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 4. Executar a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### 5. Acessar a documentação da API

```
http://localhost:8080/swagger-ui/index.html
```

## Endpoints disponíveis

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | /api/produtos | Lista todos os produtos |
| GET | /api/produtos/{id} | Busca produto por ID |
| POST | /api/produtos | Cadastra novo produto |
| PUT | /api/produtos/{id} | Atualiza produto existente |
| DELETE | /api/produtos/{id} | Remove produto |

## Exemplo de requisição

### Cadastrar produto

```json
POST /api/produtos
{
  "nome": "Notebook",
  "descricao": "Notebook gamer 16GB RAM",
  "preco": 4500.00,
  "quantidade": 10,
  "categoria": "Eletrônicos",
  "ativo": true
}
```

### Resposta

```json
{
  "id": 1,
  "nome": "Notebook",
  "descricao": "Notebook gamer 16GB RAM",
  "preco": 4500.00,
  "quantidade": 10,
  "categoria": "Eletrônicos",
  "ativo": true,
  "criadoEm": "2025-06-04T10:00:00",
  "atualizadoEm": "2025-06-04T10:00:00"
}
```

## Como executar os testes

```bash
./mvnw test
```

## Estrutura do projeto

```
src/
├── main/
│   ├── java/com/back/cadastroeestoque/
│   │   ├── config/         # Configurações (Swagger)
│   │   ├── controller/     # Endpoints da API
│   │   ├── dto/            # Objetos de transferência de dados
│   │   ├── exception/      # Exceções e tratamento de erros
│   │   ├── mapper/         # Conversão entre entidade e DTO
│   │   ├── model/          # Entidades JPA
│   │   ├── repository/     # Acesso ao banco de dados
│   │   └── service/        # Regras de negócio
│   └── resources/
│       ├── db/migration/   # Scripts Flyway
│       └── application.properties
└── test/
    └── java/com/back/cadastroeestoque/
        ├── controller/     # Testes de integração
        └── service/        # Testes unitários
```
