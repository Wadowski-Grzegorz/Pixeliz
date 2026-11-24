INSERT INTO role (name, admin, read, write, delete)
    VALUES
        ('owner', true, true, true, true),
        ('moderator', true, true, true, false),
        ('painter', false, true, true, false),
        ('viewer', false, true, false, false)
    ON CONFLICT DO NOTHING;