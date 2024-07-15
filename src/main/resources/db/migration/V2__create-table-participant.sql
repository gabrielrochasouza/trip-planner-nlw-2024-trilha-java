CREATE TABLE participants (
    id UUID default uuid_generate_v4() PRIMARY KEY,
    name VARCHAR(255) not null,
    email VARCHAR(255) not null,
    is_confirmed BOOLEAN not null default false,
    trip_id UUID,
    FOREIGN KEY (trip_id) REFERENCES trips(id)
);