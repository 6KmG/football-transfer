-- 6. feladat: atigazolast ervenyesito dokumentum tabla + futo sorszam szekvencia
-- Dokumentum azonosito formatum: prefix + fix hosszusagu, nullakkal feltoltott futo sorszam + suffix
-- pelda: ATIG000014HU (prefix=ATIG, sorszam 6 jegyu, suffix=HU)

CREATE SEQUENCE transfer_document_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE transfer_documents (
    id BIGSERIAL PRIMARY KEY,
    document_number VARCHAR(20) NOT NULL,
    sequence_number BIGINT NOT NULL,
    transfer_date DATE NOT NULL,
    player_id BIGINT NOT NULL,
    source_team_id BIGINT,
    target_team_id BIGINT NOT NULL,
    fee NUMERIC(15, 2) NOT NULL,
    transfer_request_id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_transfer_document_number UNIQUE (document_number),
    CONSTRAINT uq_transfer_document_sequence UNIQUE (sequence_number),
    CONSTRAINT uq_transfer_document_request UNIQUE (transfer_request_id),
    CONSTRAINT fk_transfer_document_player FOREIGN KEY (player_id) REFERENCES players (id) ON DELETE CASCADE,
    CONSTRAINT fk_transfer_document_source_team FOREIGN KEY (source_team_id) REFERENCES teams (id) ON DELETE SET NULL,
    CONSTRAINT fk_transfer_document_target_team FOREIGN KEY (target_team_id) REFERENCES teams (id) ON DELETE CASCADE,
    CONSTRAINT fk_transfer_document_request FOREIGN KEY (transfer_request_id) REFERENCES transfer_requests (id) ON DELETE CASCADE,
    CONSTRAINT chk_transfer_document_fee CHECK (fee >= 0)
);

CREATE INDEX idx_transfer_document_player_id ON transfer_documents (player_id);
CREATE INDEX idx_transfer_document_source_team_id ON transfer_documents (source_team_id);
CREATE INDEX idx_transfer_document_target_team_id ON transfer_documents (target_team_id);
CREATE INDEX idx_transfer_document_request_id ON transfer_documents (transfer_request_id);
CREATE INDEX idx_transfer_document_number ON transfer_documents (document_number);
