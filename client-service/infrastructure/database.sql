CREATE SCHEMA IF NOT EXISTS client;

CREATE TABLE client.Client (
    id           BIGINT          NOT NULL            AUTO_INCREMENT PRIMARY KEY,
    public_key   VARCHAR(36)     NOT NULL            UNIQUE,
    cpf          VARCHAR(11)     NOT NULL            UNIQUE,
    name         VARCHAR(255)    NOT NULL,

    updated_at   TIMESTAMP(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    created_at   TIMESTAMP(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
);

CREATE INDEX idx_name_client ON client.Client (name);
