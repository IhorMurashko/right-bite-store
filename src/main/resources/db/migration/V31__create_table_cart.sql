CREATE TABLE carts
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('cart_seq'),
    user_id     BIGINT    NOT NULL UNIQUE,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP,
    total_price NUMERIC(19, 2),
    CONSTRAINT fk_carts_users FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE
);