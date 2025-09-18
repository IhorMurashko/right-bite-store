ALTER TABLE Product DROP COLUMN weight;
ALTER TABLE Product ADD COLUMN weight NUMERIC(6,2);

UPDATE Product
SET weight = round((random() * (2000 - 100) + 100)::numeric, 2);