CREATE TABLE IF NOT EXISTS Product
(
    id                BIGSERIAL PRIMARY KEY,
    product_name      VARCHAR(123)   NOT NULL,
    price             NUMERIC(10, 2) NOT NULL,
    description       TEXT           NOT NULL,
    kcal              INTEGER        NOT NULL,
    quantity_in_stock INTEGER          DEFAULT 0,
    rating            DOUBLE PRECISION DEFAULT 0,
    rating_count      INTEGER          DEFAULT 0,
    brand_id          BIGINT REFERENCES BRAND (ID),
    producer_id       BIGINT REFERENCES PRODUCER (ID)
);