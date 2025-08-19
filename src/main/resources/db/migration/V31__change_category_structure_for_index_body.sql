CREATE TYPE body_index AS ENUM ('Underweight', 'Normal', 'Overweight');

ALTER TABLE Category ADD COLUMN index_body body_index;

