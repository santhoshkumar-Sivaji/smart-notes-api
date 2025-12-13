ALTER TABLE users ADD COLUMN username VARCHAR(255);
UPDATE users SET username = 'user' || id;
ALTER TABLE users ALTER COLUMN username SET NOT NULL;
ALTER TABLE users ADD CONSTRAINT uq_username UNIQUE (username);
