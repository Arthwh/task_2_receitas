IF (
   SELECT *
   FROM usuario
   WHERE login = 'administrador@administrador.com'
   LIMIT 1
)
   BEGIN
       INSERT INTO usuario (nome, login, senha, situacao) VALUES ('Administrador', 'administrador@administrador.com', '$2a$10$ns1IzzY4SnP/JAAxUuhnF.EYYTqcsQTkbz/BlKhPVgvt6p53F9LGe', 'ATIVO');
   END