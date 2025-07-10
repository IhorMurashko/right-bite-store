--liquibase formatted sql

--changeset eloshonkov:1
CREATE TABLE IF NOT EXISTS Brand  (
    id BIGSERIAL PRIMARY KEY,
    brand_name VARCHAR(123) NOT NULL
);

--changeset eloshonkov:2
CREATE TABLE IF NOT EXISTS Category(
    id BIGSERIAL PRIMARY KEY,
    category_name VARCHAR(123) NOT NULL,
    image VARCHAR(123) NOT NULL
);

--changeset eloshonkov:3
CREATE TABLE IF NOT EXISTS Producer(
    id BIGSERIAL PRIMARY KEY,
    producer_name VARCHAR(123) NOT NULL
);
--changeset eloshonkov:4
CREATE TABLE IF NOT EXISTS Product(
    id BIGSERIAL PRIMARY KEY,
    product_name VARCHAR(123) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    description TEXT NOT NULL,
    kcal INTEGER NOT NULL,
    quantity_in_stock INTEGER DEFAULT 0,
    rating DOUBLE PRECISION DEFAULT 0,
    rating_count INTEGER DEFAULT 0,
    brand_id BIGINT REFERENCES BRAND(ID),
    producer_id BIGINT REFERENCES PRODUCER(ID)
);

--changeset eloshonkov:5
CREATE TABLE IF NOT EXISTS Image(
     id BIGSERIAL PRIMARY KEY,
     url VARCHAR(200) NOT NULL,
     product_id BIGINT REFERENCES Product(id)
);

--changeset eloshonkov:6
CREATE TABLE IF NOT EXISTS Product_sales(
    id BIGSERIAL PRIMARY KEY,
    quantity INTEGER NOT NULL DEFAULT 0,
    sale_date DATE,
    product_id BIGINT REFERENCES Product(id)
);

--changeset eloshonkov:7
CREATE TABLE IF NOT EXISTS Product_category(
    product_id BIGINT REFERENCES Product(id),
    category_id BIGINT REFERENCES Category(id),
    PRIMARY KEY (product_id, category_id)
);