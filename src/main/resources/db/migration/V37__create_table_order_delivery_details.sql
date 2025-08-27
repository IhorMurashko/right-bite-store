CREATE SEQUENCE orders_delivery_details_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE orders
(
    id        BIGINT PRIMARY KEY,
    firstname VARCHAR(50) NOT NULL,
    lastname  VARCHAR(50),
    phone_number VARCHAR(15) NOT NULL ,
    house_number VARCHAR(10),
    street_name VARCHAR(100),
    city VARCHAR(50),
    postcode VARCHAR(10),
    user_id BIGINT NOT NULL,
    order_delivery_details BIGINT,

    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT,
    CONSTRAINT order_delivery_details_id_fk FOREIGN KEY (order_delivery_details)
        REFERENCES order_delivery_details (id) ON DELETE CASCADE
)