CREATE SEQUENCE news_letter_subscriptions_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE newsletter_subscriptions
(
    id            BIGINT PRIMARY KEY,
    email         VARCHAR(100) NOT NULL,
    is_subscribed BOOLEAN      NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP
)