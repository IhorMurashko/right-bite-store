INSERT INTO roles (id, name, description)
VALUES (1, 'ROLE_ADMIN', 'admin role');

INSERT INTO roles (id, name, description)
VALUES (2, 'ROLE_USER', 'standard user role');


-- ======= CREATE TABLES =======
CREATE TABLE IF NOT EXISTS Brand  (
id BIGSERIAL PRIMARY KEY,
brand_name VARCHAR(123) NOT NULL
);

CREATE TABLE IF NOT EXISTS Category(
id BIGSERIAL PRIMARY KEY,
category_name VARCHAR(123) NOT NULL,
image VARCHAR(123) NOT NULL
);

CREATE TABLE IF NOT EXISTS Producer(
id BIGSERIAL PRIMARY KEY,
producer_name VARCHAR(123) NOT NULL
);

CREATE TABLE IF NOT EXISTS Product(
id BIGSERIAL PRIMARY KEY,
product_name VARCHAR(123) NOT NULL,
price NUMERIC(10,2) NOT NULL,
description TEXT NOT NULL,
kcal INTEGER NOT NULL,
quantity_in_stock INTEGER DEFAULT 0,
rating DOUBLE PRECISION DEFAULT 0,
rating_count INTEGER DEFAULT 0,
brand_id BIGINT REFERENCES BRAND(ID),
producer_id BIGINT REFERENCES PRODUCER(ID)
);

CREATE TABLE IF NOT EXISTS Image(
id BIGSERIAL PRIMARY KEY,
url VARCHAR(200) NOT NULL,
product_id BIGINT REFERENCES Product(id)
);

CREATE TABLE IF NOT EXISTS Product_sales(
id BIGSERIAL PRIMARY KEY,
quantity INTEGER NOT NULL DEFAULT 0,
sale_date DATE,
product_id BIGINT REFERENCES Product(id)
);

CREATE TABLE IF NOT EXISTS Product_category(
product_id BIGINT REFERENCES Product(id),
category_id BIGINT REFERENCES Category(id),
PRIMARY KEY (product_id, category_id)
);

-- ======= ALTERS =======
ALTER TABLE brand ADD UNIQUE(brand_name);
ALTER TABLE Category ADD UNIQUE(category_name);
ALTER TABLE Producer ADD UNIQUE(producer_name);

-- ======= INSERT PRODUCER =======
INSERT INTO producer (producer_name) VALUES
('Nestle'),
('Mars'),
('Oils'),
('Test'),
('BioWorld');

-- ======= INSERT BRAND =======
INSERT INTO brand (brand_name) VALUES
('Biona'),
('LaSelva'),
('IsoaBia'),
('Terrarton'),
('NaturLine');

-- ======= INSERT CATEGORY =======
INSERT INTO category (category_name, image) VALUES
('Молочные продукты', 'milk.jpg'),
('Хлебобулочные изделия', 'bread.jpg'),
('Напитки', 'drink.jpg'),
('Орехи и сухофрукты', 'nuts.jpg'),
('Масла', 'oil.jpg'),
('Сладости', 'sweets.jpg'),
('Фрукты и овощи', 'veggies.jpg'),
('Мясо и рыба', 'meat.jpg');

-- ======= INSERT PRODUCT =======
INSERT INTO product (product_name, price, description, quantity_in_stock, kcal, rating, rating_count, producer_id, brand_id) VALUES
('Молоко', 23.30, 'Пастеризованное молоко отборного качества', 100, 65, 5.6, 32, 1, 1),
('Хлеб', 10.30, 'Ржаной хлеб с тмином', 200, 230, 4.8, 27, 2, 1),
('Чай', 22.60, 'Чёрный байховый чай без добавок', 150, 0, 4.1, 15, 4, 3),
('Орехи', 15.00, 'Микс из орехов и сухофруктов', 120, 500, 4.5, 12, 3, 2),
('Шоколад', 12.50, 'Молочный шоколад с орехами', 80, 400, 4.7, 19, 2, 2),
('Миндальное молоко', 18.90, 'Растительное молоко без сахара', 90, 30, 4.9, 22, 1, 3),
('Йогурт', 9.80, 'Натуральный йогурт без добавок', 75, 90, 4.6, 18, 1, 1),
('Сыр', 25.00, 'Пармезан выдержанный', 60, 300, 4.4, 11, 5, 4),
('Сок', 14.70, 'Томатный сок', 140, 20, 4.2, 14, 4, 4),
('Оливковое масло', 35.00, 'Extra Virgin Olive Oil', 50, 0, 5.0, 29, 3, 5);

-- ======= INSERT PRODUCT_CATEGORY =======
INSERT INTO product_category (product_id, category_id) VALUES
(1, 1), -- Молоко -> Молочка
(2, 2), -- Хлеб -> Хлебобулка
(3, 3), -- Чай -> Напитки
(4, 4), -- Орехи -> Орехи и сухофрукты
(5, 6), -- Шоколад -> Сладости
(6, 1), -- Миндальное молоко -> Молочка
(7, 1), -- Йогурт -> Молочка
(8, 1), -- Сыр -> Молочка
(9, 3), -- Сок -> Напитки
(10, 5);-- Масло -> Масла

-- ======= INSERT IMAGE =======
INSERT INTO image (url, product_id) VALUES
('https://example.com/images/milk.jpg', 1),
('https://example.com/images/bread.jpg', 2),
('https://example.com/images/tea.jpg', 3),
('https://example.com/images/nuts.jpg', 4),
('https://example.com/images/chocolate.jpg', 5),
('https://example.com/images/almond_milk.jpg', 6),
('https://example.com/images/yogurt.jpg', 7),
('https://example.com/images/parmesan.jpg', 8),
('https://example.com/images/tomato_juice.jpg', 9),
('https://example.com/images/olive_oil.jpg', 10);

-- ======= INSERT PRODUCT_SALES =======
INSERT INTO product_sales (product_id, quantity, sale_date) VALUES
(1, 55, '2024-07-01'),
(2, 15, '2024-07-02'),
(3, 40, '2024-07-03'),
(4, 5,  '2024-07-04'),
(5, 13, '2024-07-05'),
(6, 18, '2024-07-06'),
(7, 22, '2024-07-07'),
(8, 6,  '2024-07-08'),
(9, 33, '2024-07-09'),
(10, 9, '2024-07-10');