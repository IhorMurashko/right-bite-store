CREATE TABLE IF NOT EXISTS Image
(
    id         BIGSERIAL PRIMARY KEY,
    url        VARCHAR(200) NOT NULL,
    product_id BIGINT REFERENCES Product (id)
);