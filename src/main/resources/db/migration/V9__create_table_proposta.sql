CREATE TABLE proposta(
	id_proposta BIGSERIAL PRIMARY KEY,
	descricao_proposta VARCHAR(210) NOT NULL,
	valor_proposta DECIMAL(19, 2) NULL
);