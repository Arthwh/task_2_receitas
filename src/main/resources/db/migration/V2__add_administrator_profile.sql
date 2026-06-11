INSERT INTO usuario (nome, login, senha, situacao)
VALUES ('Administrador', 'administrador@administrador.com', '$2a$10$ns1IzzY4SnP/JAAxUuhnF.EYYTqcsQTkbz/BlKhPVgvt6p53F9LGe', 'ATIVO')
ON CONFLICT (login) DO NOTHING;