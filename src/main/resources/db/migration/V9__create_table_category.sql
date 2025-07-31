CREATE TABLE IF NOT EXISTS Category
(
    id            BIGSERIAL PRIMARY KEY,
    category_name VARCHAR(123) NOT NULL,
    image         VARCHAR(123) NOT NULL
);