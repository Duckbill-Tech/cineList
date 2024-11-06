CREATE TABLE filme (
   id           BINARY(16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
   titulo       TEXT NOT NULL,
   descricao    VARCHAR(255),
   created_by   BINARY(16),
   updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   completed_at TIMESTAMP,
   deleted_at   TIMESTAMP,
   nota         DOUBLE,
   FOREIGN KEY (created_by) REFERENCES usuario(id) ON DELETE SET NULL
);