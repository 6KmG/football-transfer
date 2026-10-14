-- 5. feladat: atigazolasi kerelem tabla
CREATE TABLE transfer_requests (
    id BIGSERIAL PRIMARY KEY,
    player_id BIGINT NOT NULL,
    source_team_id BIGINT,
    target_team_id BIGINT NOT NULL,
    fee NUMERIC(15, 2) NOT NULL,
    created_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_transfer_player FOREIGN KEY (player_id) REFERENCES players (id) ON DELETE CASCADE,
    CONSTRAINT fk_transfer_source_team FOREIGN KEY (source_team_id) REFERENCES teams (id) ON DELETE SET NULL,
    CONSTRAINT fk_transfer_target_team FOREIGN KEY (target_team_id) REFERENCES teams (id) ON DELETE CASCADE,
    CONSTRAINT chk_transfer_fee CHECK (fee >= 0),
    CONSTRAINT chk_transfer_expiry CHECK (expiry_date >= created_date),
    CONSTRAINT chk_transfer_status CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED', 'CLOSED'))
);

CREATE INDEX idx_transfer_player_id ON transfer_requests (player_id);
CREATE INDEX idx_transfer_source_team_id ON transfer_requests (source_team_id);
CREATE INDEX idx_transfer_target_team_id ON transfer_requests (target_team_id);
CREATE INDEX idx_transfer_status ON transfer_requests (status);
