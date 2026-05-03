CREATE TABLE especialidade (
    id_especialidade BIGSERIAL PRIMARY KEY,
    nome_especialidade VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE area(
    id_area BIGSERIAL PRIMARY KEY,
    nome_area VARCHAR(255) NOT NULL UNIQUE,
    descricao_area TEXT NOT NULL
);

CREATE TABLE termo_postagem (
    id_termo_postagem BIGSERIAL PRIMARY KEY,
    titulo_termo_postagem VARCHAR(255) NOT NULL UNIQUE,
    descricao_termo_postagem TEXT NOT NULL
);

CREATE TABLE termo_vinculo(
    id_termo_vinculo BIGSERIAL PRIMARY KEY,
    titulo_termo_vinculo VARCHAR(255) NOT NULL UNIQUE,
    descricao_termo_vinculo TEXT NOT NULL
);

CREATE TABLE tipo_projeto(
    id_tipo_projeto BIGSERIAL PRIMARY KEY,
    nome_tipo_projeto VARCHAR(255) NOT NULL UNIQUE,
    descricao_tipo_projeto TEXT NOT NULL
);




CREATE TABLE tipo_vinculo (
    id_tipo_vinculo BIGSERIAL PRIMARY KEY,
    nome_tipo_vinculo VARCHAR(50) NOT NULL UNIQUE
);

-- Inserindo os dados iniciais como no ENUM do diagrama
INSERT INTO tipo_vinculo (nome_tipo_vinculo) VALUES ('CRIADOR'), ('SOCIO'), ('INVESTIDOR');




CREATE TABLE status (
    id_status BIGSERIAL PRIMARY KEY,
    nome_status VARCHAR(50) NOT NULL UNIQUE
);

-- Inserindo os dados iniciais como no ENUM do diagrama
INSERT INTO status (nome_status) VALUES ('INICIADO'), ('EM_ANDAMENTO'), ('FINALIZADO');




CREATE TABLE role (
    id_role BIGSERIAL PRIMARY KEY,
    nome_role VARCHAR(50) NOT NULL UNIQUE
);

-- Inserindo os dados iniciais como no ENUM do diagrama
INSERT INTO role (nome_role) VALUES ('EMPRESA'), ('USUARIO'), ('ADMIN');
