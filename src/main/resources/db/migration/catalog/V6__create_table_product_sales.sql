CREATE TABLE IF NOT EXISTS Product_sales(
    id BIGSERIAL PRIMARY KEY,
    quantity INTEGER NOT NULL DEFAULT 0,
    sale_date DATE,
    product_id BIGINT REFERENCES Product(id)
);
