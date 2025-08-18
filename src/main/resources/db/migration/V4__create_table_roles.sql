CREATE TABLE roles
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('roles_seq'),
    name        VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255)
);