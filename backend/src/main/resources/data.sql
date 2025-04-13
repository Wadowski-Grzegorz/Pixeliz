INSERT INTO role (name, admin, read, write, delete)
    VALUES
        ('owner', true, true, true, true),
        ('moderator', true, true, true, false),
        ('painter', false, true, true, false),
        ('viewer', false, true, false, false)
    ON CONFLICT DO NOTHING;
-- INSERT INTO role (name, admin, read, write, delete)
--     VALUES ('moderator', true, true, true, false)
--     ON CONFLICT DO NOTHING;
-- INSERT INTO role (name, admin, read, write, delete)
--     VALUES ('painter', false, true, true, false)
--     ON CONFLICT DO NOTHING;
-- INSERT INTO role (name, admin, read, write, delete)
--     VALUES ('viewer', false, true, false, false)
--     ON CONFLICT DO NOTHING;