ALTER TABLE teams
    ADD COLUMN budget NUMERIC(15, 2) NOT NULL DEFAULT 0;

ALTER TABLE teams
    ADD CONSTRAINT chk_team_founded_year CHECK (founded_year >= 1850);
