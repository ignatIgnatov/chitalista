-- Insert default admin user
-- Password: admin123 (BCrypt hashed)

INSERT INTO users (username, password, role, enabled)
VALUES (
    'admin',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'ROLE_ADMIN',
    true
)
ON CONFLICT (username) DO NOTHING;