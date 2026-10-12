CREATE TABLE players (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    position VARCHAR(10) NOT NULL,
    shirt_number INTEGER NOT NULL,
    birth_date DATE NOT NULL,
    market_value NUMERIC(15, 2) NOT NULL,
    team_id BIGINT,
    CONSTRAINT chk_player_position CHECK (position IN ('GK', 'DF', 'MF', 'FW')),
    CONSTRAINT chk_player_shirt_number CHECK (shirt_number BETWEEN 1 AND 99),
    CONSTRAINT chk_player_market_value CHECK (market_value >= 0),
    CONSTRAINT fk_player_team FOREIGN KEY (team_id) REFERENCES teams (id) ON DELETE SET NULL
);

CREATE INDEX idx_player_team_id ON players (team_id);
