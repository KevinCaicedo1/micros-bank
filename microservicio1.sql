

-- Enable the uuid-ossp extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- Create the Person table
CREATE TABLE Person (
    id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),  -- Unique Identifier using UUID
    person_name VARCHAR(100) NOT NULL,
    gender VARCHAR(10),  -- Assuming we have gender categories
    identification VARCHAR(10) UNIQUE NOT NULL,
    person_address VARCHAR(255),
    phone VARCHAR(15)
);

-- Create the Client table which inherits from Person
CREATE TABLE Customer (
    customer_id UUID NOT NULL,  -- Unique Identifier for Client
    person_id UUID NOT NULL REFERENCES Person(id) ON DELETE CASCADE, -- Foreign key reference to Person
    customer_password VARCHAR(100) NOT NULL,
    customer_status BOOLEAN NOT NULL
);

INSERT INTO Person (id, person_name, gender, identification, person_address, phone) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'Juan Perez', 'M',  '123456789', 'Quito', '555-1234'),
('550e8400-e29b-41d4-a716-446655440001', 'Maria Lopez', 'F',  '987654321', 'Ambato', '555-5678'),
('550e8400-e29b-41d4-a716-446655440002', 'Carlos Gomez', 'M',  '112233445', 'Sangolqui', '555-8765'),
('550e8400-e29b-41d4-a716-446655440099', 'Andrea Pasto', 'F', '221133445', 'Sangolqui', '444-8765');

INSERT INTO Customer (customer_id, person_id, customer_password, customer_status) VALUES
('550e8400-e29b-41d4-a716-446655440100', '550e8400-e29b-41d4-a716-446655440000', '123456', TRUE),
('550e8400-e29b-41d4-a716-446655440101', '550e8400-e29b-41d4-a716-446655440001', '654321', TRUE),
('550e8400-e29b-41d4-a716-446655440102', '550e8400-e29b-41d4-a716-446655440002', '112233', TRUE),
('550e8400-e29b-41d4-a716-446655440199', '550e8400-e29b-41d4-a716-446655440099', '221133', TRUE);