CREATE TABLE cart_items
(
    id                  BIGINT PRIMARY KEY DEFAULT nextval('item_seq'),
    product_id          BIGINT         NOT NULL,
    product_name        VARCHAR(100)   NOT NULL,
    quantity            INTEGER        NOT NULL,
    unit_price_snapshot NUMERIC(19, 2) NOT NULL,
    price_snapshot_time TIMESTAMP      NOT NULL,
    total_price         NUMERIC(19, 2) NOT NULL,
    thumbnail_url       VARCHAR(100)   NOT NULL,
    cart_id             BIGINT         NOT NULL,

    CONSTRAINT fk_cart_items_products FOREIGN KEY (product_id)
        REFERENCES product (id) ON DELETE RESTRICT,

    CONSTRAINT fk_cart_items_carts FOREIGN KEY (cart_id)
        REFERENCES carts (id) ON DELETE CASCADE
);