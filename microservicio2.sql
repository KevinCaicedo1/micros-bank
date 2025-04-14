CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- Create the Account table
CREATE TABLE Account (
    account_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),  -- Unique Identifier for Account
    account_number VARCHAR(50) UNIQUE NOT NULL,
    account_type VARCHAR(50),
    initial_balance DECIMAL(10, 2) NOT NULL,
    account_status BOOLEAN NOT NULL,
    client_id UUID NOT NULL
);

-- Create the Movement table
CREATE TABLE Movement (
    movement_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),  -- Unique Identifier for Movement
    account_id UUID REFERENCES Account(account_id) ON DELETE CASCADE,  -- Foreign key reference to Account
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    movement_type VARCHAR(10) CHECK (movement_type IN ('DEPOSIT', 'WITHDRAWAL')) NOT NULL,
    movement_value DECIMAL(10, 2) NOT NULL,
    initial_balance DECIMAL(10, 2) NOT NULL,
    available_balance DECIMAL(10, 2) NOT NULL
);

INSERT INTO Account (account_id, account_number, account_type, initial_balance, account_status, client_id) VALUES
('550e8400-e29b-41d4-a716-446655440200', '100-100-100', 'AHORRO', 1000.00, TRUE, '550e8400-e29b-41d4-a716-446655440100'),
('550e8400-e29b-41d4-a716-446655440201', '200-200-200', 'CORRIENTE', 2000.00, TRUE, '550e8400-e29b-41d4-a716-446655440101'),
('550e8400-e29b-41d4-a716-446655440202', '300-300-300', 'AHORRO', 3000.00, TRUE, '550e8400-e29b-41d4-a716-446655440102'),
('550e8400-e29b-41d4-a716-446655440299', '400-400-400', 'CORRIENTE', 4000.00, TRUE, '550e8400-e29b-41d4-a716-446655440199');

INSERT INTO Movement (movement_id, account_id, movement_type, movement_value, initial_balance, available_balance) VALUES
('550e8400-e29b-41d4-a716-446655440300', '550e8400-e29b-41d4-a716-446655440200', 'DEPOSIT', 100.00, 1000.00, 1100.00),
('550e8400-e29b-41d4-a716-446655440301', '550e8400-e29b-41d4-a716-446655440201', 'DEPOSIT', 200.00, 2000.00, 2200.00),
('550e8400-e29b-41d4-a716-446655440302', '550e8400-e29b-41d4-a716-446655440202', 'DEPOSIT', 300.00, 3000.00, 3300.00),
('550e8400-e29b-41d4-a716-446655440399', '550e8400-e29b-41d4-a716-446655440299', 'DEPOSIT', 400.00, 4000.00, 4400.00);