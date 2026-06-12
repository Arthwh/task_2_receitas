INSERT INTO usuario (nome, login, senha, situacao)
SELECT 'Administrador', 'administrador@administrador.com', '$2a$10$ns1IzzY4SnP/JAAxUuhnF.EYYTqcsQTkbz/BlKhPVgvt6p53F9LGe', 'ATIVO'
    WHERE NOT EXISTS (
    SELECT 1 FROM usuario WHERE login = 'administrador@administrador.com'
);