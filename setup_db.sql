-- Criação das tabelas para o SAIM DB

CREATE TABLE IF NOT EXISTS usuarios (
    usu_id SERIAL PRIMARY KEY, 
    usu_nome VARCHAR(255) NOT NULL, 
    usu_cpf VARCHAR(14) UNIQUE NOT NULL, 
    usu_senha VARCHAR(255) NOT NULL, 
    usu_tipo VARCHAR(50) NOT NULL, -- admin, medico, etc.
    usu_data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS medicos ( 
    med_id SERIAL PRIMARY KEY, 
    usu_id INT NOT NULL REFERENCES usuarios(usu_id), 
    med_nome VARCHAR(255) NOT NULL, 
    med_crm VARCHAR(50) UNIQUE NOT NULL, 
    med_especialidade VARCHAR(255) NOT NULL, 
    med_telefone VARCHAR(20), 
    med_email VARCHAR(255), 
    med_horario_atendimento VARCHAR(255), 
    med_data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    med_ativo BOOLEAN DEFAULT true 
);

CREATE TABLE IF NOT EXISTS caixa ( 
    cai_id SERIAL PRIMARY KEY, 
    usu_abertura_id INT NOT NULL REFERENCES usuarios(usu_id), 
    usu_fechamento_id INT REFERENCES usuarios(usu_id), 
    data_abertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    data_fechamento TIMESTAMP, 
    saldo_inicial NUMERIC(10, 2) NOT NULL, 
    saldo_final NUMERIC(10, 2), 
    observacoes TEXT, 
    status VARCHAR(20) DEFAULT 'aberto' NOT NULL CHECK (status IN ('aberto', 'fechado')) 
);

CREATE TABLE IF NOT EXISTS movimentacoes_caixa ( 
    mov_id SERIAL PRIMARY KEY, 
    cai_id INT NOT NULL REFERENCES caixa(cai_id), 
    usu_registro_id INT NOT NULL REFERENCES usuarios(usu_id), 
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('entrada', 'saida')), 
    descricao VARCHAR(255) NOT NULL, 
    valor NUMERIC(10, 2) NOT NULL 
);

CREATE TABLE IF NOT EXISTS dados_hospital (
    dh_id SERIAL PRIMARY KEY,
    dh_email VARCHAR(255),
    dh_telefone VARCHAR(20),
    dh_endereco VARCHAR(500),
    dh_horario_inicio VARCHAR(5),
    dh_horario_fim VARCHAR(5),
    dh_cnpj VARCHAR(18),
    dh_data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS aparencia_sistema (
    as_id SERIAL PRIMARY KEY,
    as_nome VARCHAR(255),
    as_logo_base64 TEXT,
    as_cor VARCHAR(7),
    as_rodape TEXT,
    as_data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pacientes (
    pac_id SERIAL PRIMARY KEY,
    pac_nome VARCHAR(255) NOT NULL,
    pac_cpf VARCHAR(14) UNIQUE NOT NULL,
    pac_data_nascimento DATE,
    pac_telefone VARCHAR(20),
    pac_email VARCHAR(255),
    pac_endereco VARCHAR(500),
    pac_data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS prontuarios (
    pront_id SERIAL PRIMARY KEY,
    pac_cpf VARCHAR(14) NOT NULL REFERENCES pacientes(pac_cpf),
    med_id INT NOT NULL REFERENCES medicos(med_id),
    usu_registro_id INT NOT NULL REFERENCES usuarios(usu_id),
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    descricao TEXT NOT NULL,
    diagnostico TEXT,
    prescricao TEXT
);

CREATE TABLE IF NOT EXISTS consultas (
    cons_id SERIAL PRIMARY KEY,
    pac_cpf VARCHAR(14) NOT NULL REFERENCES pacientes(pac_cpf),
    med_id INT NOT NULL REFERENCES medicos(med_id),
    data_hora TIMESTAMP NOT NULL,
    status VARCHAR(50) DEFAULT 'agendada' NOT NULL, -- agendada, realizada, cancelada
    observacoes TEXT,
    data_agendamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Inserir usuário de teste se não existir
INSERT INTO usuarios (usu_nome, usu_cpf, usu_senha, usu_tipo)
VALUES ('Usuário Teste', '11122233344', 'senha_hash', 'admin') -- Substituir 'senha_hash' por um hash real em produção
ON CONFLICT (usu_cpf) DO NOTHING;

-- Inserir dados padrão para tabelas de configuração se vazias
INSERT INTO dados_hospital (dh_email, dh_telefone, dh_endereco, dh_horario_inicio, dh_horario_fim, dh_cnpj)
SELECT 'email@instituicao.com', '(00) 00000-0000', 'Endereço não cadastrado', '08:00', '18:00', '00.000.000/0000-00'
WHERE NOT EXISTS (SELECT 1 FROM dados_hospital);

INSERT INTO aparencia_sistema (as_nome, as_logo_base64, as_cor, as_rodape)
SELECT 'Nome da Instituição Padrão', '', '#1f8f8f', 'Rodapé padrão do sistema'
WHERE NOT EXISTS (SELECT 1 FROM aparencia_sistema);

