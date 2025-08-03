ALTER TABLE brand
    ADD UNIQUE (brand_name);
ALTER TABLE Category
    ADD UNIQUE (category_name);
ALTER TABLE Producer
    ADD UNIQUE (producer_name);