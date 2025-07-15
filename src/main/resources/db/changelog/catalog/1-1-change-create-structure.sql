--liquibase formatted sql

--changeset eloshonkov:1
ALTER TABLE brand ADD UNIQUE(brand_name);

--changeset eloshonkov:2
ALTER TABLE Category ADD UNIQUE(category_name);

--changeset eloshonkov:3
ALTER TABLE Producer ADD UNIQUE(producer_name);