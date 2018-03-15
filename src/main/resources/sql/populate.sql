INSERT INTO
  shops (name, address)
VALUES
  ('Shop 1', 'Address shop 1');

INSERT INTO
  customers (name, surname, age)
VALUES
  ('Name 1', 'Smirnov', 16),
  ('Vlad', 'Surname 2', 41),
  ('Name 3', 'PetrOV', 24),
  ('Vova', 'Petrov', 27),
  ('Name 5', 'Surname ', 50),
  ('Vovav', 'Sidorov', 35),
  ('Dimon', 'surname7', 36);

INSERT INTO
  orders (orders_date, customers_id, shops_id)
VALUES
  ('2017-12-25', 1, 1);

INSERT INTO
  products (name, purchase_price)
VALUES
  ('Vodka Blavod',5.30),
  ('Whiskey Balvenie',15.50),
  ('Vermouth Martini',4.23),
  ('Cognac Courvoisier',15.50),
  ('Calvados',9.80);

INSERT INTO
 orders_products (orders_id, products_id, selling_price, count)
VALUES
  (1,1,6.00,3);