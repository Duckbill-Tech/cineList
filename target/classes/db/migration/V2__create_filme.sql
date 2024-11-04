CREATE TABLE filme (
   id           CHAR(36) PRIMARY KEY DEFAULT (UUID()),
   titulo       TEXT NOT NULL,
   created_by   CHAR(36),
   updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   completed_at TIMESTAMP,
   deleted_at   TIMESTAMP,
   FOREIGN KEY (created_by) REFERENCES usuario(id) ON DELETE SET NULL,
   nota         DECIMAL(2, 1) DEFAULT NULL -- com valores decimais de 0 a 10
);