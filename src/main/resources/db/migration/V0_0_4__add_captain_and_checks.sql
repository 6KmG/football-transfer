-- 4. feladat: csapatkapitany + eletkorszeru validacios CHECK-ek
-- Team.captain FK (nullable, egy csapatnak max 1 kapitanya lehet)
ALTER TABLE teams
    ADD COLUMN captain_id BIGINT;

ALTER TABLE teams
    ADD CONSTRAINT fk_team_captain FOREIGN KEY (captain_id) REFERENCES players (id) ON DELETE SET NULL;

CREATE INDEX idx_team_captain_id ON teams (captain_id);

-- founded_year nem lehet a jovoben (DB szintu vedekezes, app szinten is validalva)
ALTER TABLE teams
    ADD CONSTRAINT chk_team_founded_year_max CHECK (founded_year <= EXTRACT(YEAR FROM CURRENT_DATE)::INT);

-- jatekos legalabb 15 eves legyen (DB szintu vedekezes, app szinten is validalva)
ALTER TABLE players
    ADD CONSTRAINT chk_player_min_age CHECK (birth_date <= CURRENT_DATE - INTERVAL '15 years');
