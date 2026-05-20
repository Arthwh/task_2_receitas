CREATE TYPE status_usuario AS ENUM ('ATIVO', 'INATIVO');
CREATE TYPE tipo_receita_paladar AS ENUM ('DOCE', 'SALGADO');

CREATE TABLE usuario(
    id SERIAL NOT NULL,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    situacao status_usuario NOT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY(id)
);

CREATE TABLE receita(
    id SERIAL NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    data_registro TIMESTAMP(2),
    custo DECIMAL(10,2) NOT NULL,
    tipo_receita tipo_receita_paladar NOT NULL,
    CONSTRAINT pk_receita PRIMARY KEY (id)
);
