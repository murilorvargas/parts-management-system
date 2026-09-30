-- ATENCAO: copia sincronizada MANUALMENTE do arquivo canonico em
-- part-service/infrastructure/database.sql (usado pelo Testcontainers MySQLContainer,
-- que so aceita scripts de init a partir do classpath). Qualquer mudanca de schema
-- feita no arquivo canonico precisa ser replicada aqui manualmente.

CREATE SCHEMA IF NOT EXISTS part;

CREATE TABLE part.Part (
    id                     BIGINT          NOT NULL            AUTO_INCREMENT PRIMARY KEY,
    public_key             VARCHAR(36)     NOT NULL            UNIQUE,
    identification_number  VARCHAR(50)     NOT NULL            UNIQUE,
    name                   VARCHAR(255)    NOT NULL,
    description            VARCHAR(1000)   NULL,

    updated_at             TIMESTAMP(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    created_at             TIMESTAMP(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
);

CREATE INDEX idx_name_part ON part.Part (name);
