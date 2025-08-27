CREATE SEQUENCE orders_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE orders
(
    id           BIGINT PRIMARY KEY,
    user_id      BIGINT,
    order_status VARCHAR(20)    NOT NULL,
    total_price  NUMERIC(19, 2) NOT NULL,
    created_at   TIMESTAMP      NOT NULL,
    updated_at   TIMESTAMP      NOT NULL,

    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT
)