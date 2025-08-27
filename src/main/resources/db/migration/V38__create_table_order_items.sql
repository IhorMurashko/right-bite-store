CREATE SEQUENCE order_item_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE order_items
(
    id             BIGINT PRIMARY KEY,
    product_id     BIGINT         NOT NULL,
    quantity       INT            NOT NULL,
    product_name   VARCHAR(50)    NOT NULL,
    price_snapshot NUMERIC(19, 2) NOT NULL,
    order_id       BIGINT         NOT NULL,

    CONSTRAINT order_id_fk FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT order_product_unique UNIQUE (order_id, product_id)
)