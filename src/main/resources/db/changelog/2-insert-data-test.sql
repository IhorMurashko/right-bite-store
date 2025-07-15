--liquibase formatted sql

--changeset eloshonkov:1
INSERT INTO  producer  (producer_name) VALUES
    ('Nestle'),
    ('Mars'),
    ('Oils'),
    ('Test'),
    ('BioWorld');

INSERT INTO brand (brand_name) VALUES
    ('Biona'),
    ('LaSelva'),
    ('IsoaBia'),
    ('Terrarton'),
    ('NaturLine');

INSERT INTO category (category_name, image) VALUES
    ('Молочные продукты', 'milk.jpg'),
    ('Хлебобулочные изделия', 'bread.jpg'),
    ('Напитки', 'drink.jpg'),
    ('Орехи и сухофрукты', 'nuts.jpg'),
    ('Масла', 'oil.jpg'),
    ('Сладости', 'sweets.jpg'),
    ('Фрукты и овощи', 'veggies.jpg'),
    ('Мясо и рыба', 'meat.jpg');

INSERT INTO product (product_name, price, description, quantity_in_stock, kcal, rating, rating_count, producer_id, brand_id) VALUES
    ('Молоко', 23.30, 'Пастеризованное молоко отборного качества', 100, 65, 5.6, 32, 1, 1),
    ('Хлеб', 10.30, 'Ржаной хлеб с тмином', 200, 230, 4.8, 27, 2, 1),
    ('Чай', 22.60, 'Чёрный байховый чай без добавок', 150, 0, 4.1, 15, 4, 3),
    ('Орехи кешью', 55.00, 'Жареные орехи кешью без соли', 300, 650, 4.2, 10, 3, 2),
    ('Шоколад горький', 44.90, 'Шоколад 70% какао, без сахара', 180, 520, 4.9, 53, 2, 2),
    ('Миндальное молоко', 33.50, 'Растительное молоко без лактозы', 90, 31, 4.5, 22, 1, 1),
    ('Йогурт клубничный', 18.20, 'Натуральный йогурт с кусочками клубники', 120, 150, 4.6, 41, 1, 1),
    ('Сыр пармезан', 78.40, 'Выдержанный сыр твердых сортов', 80, 410, 5.0, 66, 3, 4),
    ('Томатный сок', 25.00, 'Свежевыжатый сок из органических томатов', 130, 42, 4.3, 19, 4, 3),
    ('Оливковое масло', 92.70, 'Extra Virgin холодного отжима', 200, 884, 4.7, 37, 3, 2);

INSERT INTO product_category (product_id, category_id) VALUES
    (1, 7), -- молоко -> молочка
    (2, 8), -- хлеб -> хлеб
    (3, 4), -- чай -> напитки
    (4, 2), -- орехи -> орехи
    (5, 1), -- шоколад -> сладости
    (6, 3), -- миндальное молоко -> молочка
    (7, 4), -- йогурт -> молочка
    (8, 5), -- сыр -> молочка
    (9, 6), -- сок -> напитки
    (10, 7);

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

INSERT INTO product_sales (product_id, quantity, sale_date) VALUES
    (1,55, '2024-07-01 10:00:00'),
    (2, 15, '2024-07-02 11:30:00'),
    (3, 40, '2024-07-03 12:45:00'),
    (4, 5,  '2024-07-04 13:00:00'),
    (5, 13, '2024-07-05 09:20:00'),
    (6, 18, '2024-07-06 08:40:00'),
    (7, 22, '2024-07-07 15:10:00'),
    (8, 6,  '2024-07-08 16:30:00'),
    (9, 33, '2024-07-09 17:50:00'),
    (10, 9, '2024-07-10 18:15:00');