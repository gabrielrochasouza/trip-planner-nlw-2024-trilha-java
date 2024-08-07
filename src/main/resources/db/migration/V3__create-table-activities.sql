CREATE TABLE activities (
    id UUID default uuid_generate_v4() PRIMARY KEY,
    title VARCHAR(255) not null,
    occurs_at TIMESTAMP NOT NULL,
    trip_id UUID,
    FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE
);
