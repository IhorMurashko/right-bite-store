CREATE TABLE users
(
    id                         BIGINT PRIMARY KEY,
    email                      VARCHAR(255) NOT NULL UNIQUE,
    password                   VARCHAR(255),
    auth_provider              VARCHAR(255) NOT NULL,
    oauth_id                   VARCHAR(255),
    is_account_non_expired     BOOLEAN      NOT NULL DEFAULT TRUE,
    is_account_non_locked      BOOLEAN      NOT NULL DEFAULT TRUE,
    is_credentials_non_expired BOOLEAN      NOT NULL DEFAULT TRUE,
    is_enabled                 BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at                 TIMESTAMP    NOT NULL,
    updated_at                 TIMESTAMP,

    first_name                 VARCHAR(255),
    last_name                  VARCHAR(255),
    image_url                  VARCHAR(255),
    phone_number               VARCHAR(50),
    country                    VARCHAR(100),
    city                       VARCHAR(100),
    street_name                VARCHAR(100),
    house_number               VARCHAR(50),
    zip_code                   VARCHAR(20)
);
