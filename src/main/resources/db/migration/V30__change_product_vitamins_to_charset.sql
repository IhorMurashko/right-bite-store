UPDATE Product
SET vitamins = 'C'
WHERE vitamins ~ '^[0-9]+$';