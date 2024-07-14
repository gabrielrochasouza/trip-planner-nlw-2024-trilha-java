CREATE TABLE activities (
    id UUID default RANDOM_UUID() PRIMARY KEY,
    title VARCHAR(255) not null,
    occurs_at TIMESTAMP NOT NULL,
    trip_id UUID,
    FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE
);
