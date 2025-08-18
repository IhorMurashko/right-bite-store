CREATE TABLE IF NOT EXISTS billing_info
(
    id           BIGINT PRIMARY KEY,
    name_on_card VARCHAR(255),
    card_number  TEXT,
    expire_date  VARCHAR(10),
    user_id      BIGINT,
    CONSTRAINT fk_billing_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);