CREATE TABLE usuario
(
    id BIGINT(20) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    senha VARCHAR(150) NOT NULL
)
ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permissao
(
    id BIGINT(20) PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL
)
ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE usuario_permissao
(
    id_usuario BIGINT(20) NOT NULL,
    id_permissao BIGINT(20) NOT NULL,
    PRIMARY KEY (id_usuario, id_permissao),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    FOREIGN KEY (id_permissao) REFERENCES permissao(id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO usuario
    (id, nome, email, senha)
VALUES
    (1, 'Administrador', 'admin@email.com', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.');
INSERT INTO usuario
    (id, nome, email, senha)
VALUES
    (2, 'Maria Silva', 'maria@email.com', '$2a$10$Zc3w6HyuPOPXamaMhh.PQOXvDnEsadztbfi6/RyZWJDzimE8WQjaq');

INSERT INTO permissao
    (id, descricao)
VALUES
    (1, 'ROLE_CADASTRAR_CATEGORIA');
INSERT INTO permissao
    (id, descricao)
VALUES
    (2, 'ROLE_PESQUISAR_CATEGORIA');

INSERT INTO permissao
    (id, descricao)
VALUES
    (3, 'ROLE_CADASTRAR_PESSOA');
INSERT INTO permissao
    (id, descricao)
VALUES
    (4, 'ROLE_REMOVER_PESSOA');
INSERT INTO permissao
    (id, descricao)
VALUES
    (5, 'ROLE_PESQUISAR_PESSOA');

INSERT INTO permissao
    (id, descricao)
VALUES
    (6, 'ROLE_CADASTRAR_LANCAMENTO');
INSERT INTO permissao
    (id, descricao)
VALUES
    (7, 'ROLE_REMOVER_LANCAMENTO');
INSERT INTO permissao
    (id, descricao)
VALUES
    (8, 'ROLE_PESQUISAR_LANCAMENTO');

-- admin
INSERT INTO usuario_permissao
    (id_usuario, id_permissao)
VALUES
    (1, 1);
INSERT INTO usuario_permissao
    (id_usuario, id_permissao)
VALUES
    (1, 2);
INSERT INTO usuario_permissao
    (id_usuario, id_permissao)
VALUES
    (1, 3);
INSERT INTO usuario_permissao
    (id_usuario, id_permissao)
VALUES
    (1, 4);
INSERT INTO usuario_permissao
    (id_usuario, id_permissao)
VALUES
    (1, 5);
INSERT INTO usuario_permissao
    (id_usuario, id_permissao)
VALUES
    (1, 6);
INSERT INTO usuario_permissao
    (id_usuario, id_permissao)
VALUES
    (1, 7);
INSERT INTO usuario_permissao
    (id_usuario, id_permissao)
VALUES
    (1, 8);

-- maria
INSERT INTO usuario_permissao
    (id_usuario, id_permissao)
VALUES
    (2, 2);
INSERT INTO usuario_permissao
    (id_usuario, id_permissao)
VALUES
    (2, 5);
INSERT INTO usuario_permissao
    (id_usuario, id_permissao)
VALUES
    (2, 8);