/*-- Maak eerst Users
INSERT INTO users (id, username, email, password)
VALUES
    (1, 'alice', 'alice@example.com', 'test123'),
    (2, 'bob', 'bob@example.com', 'test123'),
    (3, 'charlie', 'charlie@example.com', 'test123');

-- Security Roles
INSERT INTO security_roles (id, role, user_id)
VALUES
    (1, 'TRADER', 1),
    (2, 'ADMIN', 2),
    (3, 'COMPLIANCEOFFICER', 3);

-- Wallets (gekoppeld aan users)
INSERT INTO wallets (id, wallet_adress, crypto_currency, balance, user_id)
VALUES
    (1, 'bc1qalice123', 'BTC', 500000, 1),
    (2, '0xBobEth123', 'ETH', 2000000, 2),
    (3, 'bc1qcharlie456', 'BTC', 100000, 3);

-- Orders (gekoppeld aan user + wallet)
INSERT INTO orders (id, order_type, amount, price, crypto_currency, timestamp, status, user_id, wallet_id)
VALUES
    (1, 'BUY', 1, 30000, 'BTC', '2025-08-21T10:15:00', 'OPEN', 1, 1),
    (2, 'SELL', 2, 2000, 'ETH', '2025-08-22T11:30:00', 'EXECUTED', 2, 2),
    (3, 'BUY', 1, 29000, 'BTC', '2025-08-23T14:45:00', 'CANCELLED', 3, 3);

-- KYC Files (gekoppeld aan users)
INSERT INTO kyc_files (id, file_name, file_path, file_status, user_id)
VALUES
    (1, 'alice_id.pdf', '/files/alice_id.pdf', 'APPROVED', 1),
    (2, 'bob_passport.pdf', '/files/bob_passport.pdf', 'PENDING', 2),
    (3, 'charlie_id.pdf', '/files/charlie_id.pdf', 'REJECTED', 3);*/