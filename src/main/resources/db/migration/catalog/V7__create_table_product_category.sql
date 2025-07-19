CREATE TABLE IF NOT EXISTS Product_category(
    product_id BIGINT REFERENCES Product(id),
    category_id BIGINT REFERENCES Category(id),
    PRIMARY KEY (product_id, category_id)
);