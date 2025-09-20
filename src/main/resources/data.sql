-- Maak eerst Users
INSERT INTO users (username, email, password)
VALUES
    ('alice', 'alice@example.com', '$2a$10$5dcsUKzwg0EecyR1C3XIv.Vn30.W.P86fTQm0lG0FXPru7wz7w1Pe'),
    ('bob', 'bob@example.com', '$2a$10$.Xpj2mGy/EbTPBc.jMAENO4D0Qg9BqAy6/xzBAEnPrQSHNOn1HpRq'),
    ('charlie', 'charlie@example.com', '$2a$10$Qfq4UbzW45dFDQitYmVhw.de6doth82qrYiFGaRwtqxbf/.Hbco4y');

-- Security Roles
INSERT INTO securityroles (id, role, user_id)
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
INSERT INTO kycfile (id, file_name, file_path, file_status, user_id)
VALUES
    (1, 'alice_id.pdf', '/files/alice_id.pdf', 'APPROVED', 1),
    (2, 'bob_passport.pdf', '/files/bob_passport.pdf', 'PENDING', 2),
    (3, 'charlie_id.pdf', '/files/charlie_id.pdf', 'REJECTED', 3);