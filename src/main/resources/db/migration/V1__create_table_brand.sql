
CREATE TABLE IF NOT EXISTS Brand  (
    id BIGSERIAL PRIMARY KEY,
    brand_name VARCHAR(123) NOT NULL
);
CREATE TYPE auth_provider AS ENUM ('LOCAL', 'GOOGLE');

CREATE TYPE role_name AS ENUM ('ROLE_ADMIN', 'ROLE_USER');

CREATE SEQUENCE roles_seq START WITH 1 INCREMENT BY 1;



CREATE TABLE IF NOT EXISTS roles
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('roles_seq'),
    name        role_name NOT NULL UNIQUE,
    description TEXT
);

CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS users
(
    id                         BIGINT PRIMARY KEY                   DEFAULT nextval('users_seq'),
    email                      VARCHAR(255)                NOT NULL UNIQUE,
    password                   VARCHAR(255),
    auth_provider              auth_provider               NOT NULL,
    oauth_id                   VARCHAR(255),

    is_account_non_expired     BOOLEAN                     NOT NULL DEFAULT true,
    is_account_non_locked      BOOLEAN                     NOT NULL DEFAULT true,
    is_credentials_non_expired BOOLEAN                     NOT NULL DEFAULT true,
    is_enabled                 BOOLEAN                     NOT NULL DEFAULT true,

    created_at                 TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at                 TIMESTAMP WITHOUT TIME ZONE          DEFAULT NOW(),

    first_name                 VARCHAR(255),
    last_name                  VARCHAR(255),
    image_url                  TEXT,
    phone_number               VARCHAR(50),
    country                    VARCHAR(100),
    city                       VARCHAR(100),
    street_name                VARCHAR(255),
    house_number               VARCHAR(50),
    zip_code                   VARCHAR(20)
);


CREATE TABLE IF NOT EXISTS user_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS billing_info
(
    id           BIGSERIAL PRIMARY KEY,
    name_on_card VARCHAR(255),
    card_number  TEXT,
    expire_date  VARCHAR(10),
    user_id      BIGINT,
    CONSTRAINT fk_billing_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);