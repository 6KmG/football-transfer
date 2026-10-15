ALTER TABLE teams
    ADD COLUMN captain_id BIGINT;

ALTER TABLE teams
    ADD CONSTRAINT fk_team_captain FOREIGN KEY (captain_id) REFERENCES players (id) ON DELETE SET NULL;

CREATE INDEX idx_team_captain_id ON teams (captain_id);

ALTER TABLE teams
    ADD CONSTRAINT chk_team_founded_year_max CHECK (founded_year <= EXTRACT(YEAR FROM CURRENT_DATE)::INT);

ALTER TABLE players
    ADD CONSTRAINT chk_player_min_age CHECK (birth_date <= CURRENT_DATE - INTERVAL '15 years');
