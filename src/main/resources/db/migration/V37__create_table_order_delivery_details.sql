CREATE SEQUENCE orders_delivery_details_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE order_delivery_details
(
    id              BIGINT PRIMARY KEY,
    firstname       VARCHAR(50) NOT NULL,
    lastname        VARCHAR(50),
    phone_number    VARCHAR(15) NOT NULL,
    house_number    VARCHAR(10),
    street_name     VARCHAR(100),
    city            VARCHAR(50),
    country         VARCHAR(50),
    zip_code        VARCHAR(15),
    comment         VARCHAR(150),
    delivery_method VARCHAR(20) NOT NULL,
    order_id        BIGINT      NOT NULL,

    CONSTRAINT order_id_fk FOREIGN KEY (order_id)
        REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT order_delivery_details_order_id_unique UNIQUE (order_id)
)