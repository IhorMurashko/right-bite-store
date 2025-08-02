INSERT INTO users (id, email, auth_provider, password, created_at)
VALUES (nextval('users_seq'), 'user@email.com',
        'LOCAL', '$2a$10$HXgSVxrkYHjPiKwJW/qFKuWjUYzQ37oVAtUU0Ep6w4TX3csGiz0Ju',
        current_timestamp),
       (nextval('users_seq'), 'admin@email.com',
        'LOCAL', '$2a$10$HXgSVxrkYHjPiKwJW/qFKuWjUYzQ37oVAtUU0Ep6w4TX3csGiz0Ju',
        current_timestamp);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u,
     roles r
WHERE u.email LIKE 'user@email.com'
  AND r.name LIKE 'ROLE_USER';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u,
     roles r
WHERE u.email LIKE 'admin@email.com'
  AND r.name LIKE 'ROLE_ADMIN';