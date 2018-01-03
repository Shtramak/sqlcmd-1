-- а) Найти имена покупателей, возраст которых от 16 до 35.
SELECT
  name,
  age
FROM customers
WHERE age BETWEEN 16 AND 35;

-- б) Найти имена покупателей, фамилия которых оканчивается на 'OV'
SELECT
  name,
  surname
FROM customers
WHERE lower(surname) LIKE '%ov';

-- в) Найти название самого дорогой по закупке товар, в имени которого присутствует буква 'V',
--    но не первая и не последняя.
SELECT
  name,
  purchase_price
FROM (SELECT *
      FROM products
      WHERE lower(name) SIMILAR TO '([^v]%)(v%)[^v]') AS foo
WHERE purchase_price = (SELECT MAX(purchase_price)
                        FROM products
                        WHERE lower(name) SIMILAR TO '([^v]%)(v%)[^v]');

-- г) Найти имена покупателей, в имени которых присутствует буква 'V' и не больше двух раз
SELECT name
FROM customers
WHERE (char_length(name) - char_length(replace(lower(name), 'v', ''))) BETWEEN 1 AND 2;

-- д) Найти имена покупателей, длина имени которых более 3 символов и 4 символ это 'O' (Латинская буква О)
--  и возраст менее 50 лет.
SELECT name
FROM customers
WHERE length(name) > 3 AND lower(name) LIKE '___o%' AND age < 50;