CREATE TABLE produtos (
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(100)   NOT NULL,
    descricao   VARCHAR(500),
    preco       NUMERIC(10, 2) NOT NULL,
    quantidade  INTEGER        NOT NULL DEFAULT 0,
    categoria   VARCHAR(50)    NOT NULL,
    ativo       BOOLEAN        NOT NULL DEFAULT TRUE,
    criado_em   TIMESTAMP      NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP    NOT NULL DEFAULT NOW()
);