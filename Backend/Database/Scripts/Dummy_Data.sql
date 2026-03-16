-- dummy data inserts for development/testing

-- accounts
INSERT INTO ffb.account (id, login_nr, password, type)
VALUES
  ('00000000-0000-0000-0000-000000000001','V-000-000-001', NULL, 'GUEST'),
  ('00000000-0000-0000-0000-000000000002','F-000-000-001','$2a$10$eImiTXuWVxfM37uY4JANjQ==','FOOD_COURT_WORKER'),
  ('00000000-0000-0000-0000-000000000003','A-000-000-001','$2a$10$7EQJS9B6ixkb4mvEzpFnBO','ADMIN');

-- ticket for guest
INSERT INTO ffb.ticket (ticket_id, login_nr)
VALUES
  ('11111111-1111-1111-1111-111111111111','V-000-000-001');

-- credit for guest
INSERT INTO ffb.credit (id, login_nr, amount)
VALUES
  ('22222222-2222-2222-2222-222222222222','V-000-000-001', 20.00);

-- simple cart with one item (assumes product exists)
INSERT INTO ffb.cart (id, login_nr, priority)
VALUES
  ('33333333-3333-3333-3333-333333333333','V-000-000-001', 1);

INSERT INTO ffb.cart_item (id, cart_id, product_id, count)
VALUES
  ('44444444-4444-4444-4444-444444444444','33333333-3333-3333-3333-333333333333','bf9a3924-d2ca-4aa7-a0ba-44770b081881',2);
