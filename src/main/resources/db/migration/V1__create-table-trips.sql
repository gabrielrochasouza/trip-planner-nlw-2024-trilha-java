CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE trips (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    destination VARCHAR(255) not null,
    starts_at TIMESTAMP not null,
    ends_at TIMESTAMP not null,
    is_confirmed BOOLEAN not null,
    owner_name VARCHAR(255) not null,
    owner_email VARCHAR(255) not null
);