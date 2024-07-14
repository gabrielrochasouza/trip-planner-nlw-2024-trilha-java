CREATE TABLE links (
    id UUID default RANDOM_UUID() PRIMARY KEY,
    title VARCHAR(255) not null,
    url VARCHAR(255) not null,
    trip_id UUID,
    FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE
);