INSERT INTO ffb.account
(
	account_id, login_nr, password, type
)
VALUES
(
	'6767b5c9-f58c-40cd-be15-6427648ca543', 'V-123-456-789', '123456789', 'foodCourtWorker'
);

INSERT INTO ffb.foodcourt
(
	id, account_id, display_name, image
)
VALUES
(
	'67b462a3-40c4-43a6-8373-a66e1994f21c', '6767b5c9-f58c-40cd-be15-6427648ca543', 'Burger Place', null
);

INSERT INTO ffb.product
(
	id, foodcourt_id, price, display_name, symbol, minimal_warning
)
VALUES
(
	'bf9a3924-d2ca-4aa7-a0ba-44770b081881', '67b462a3-40c4-43a6-8373-a66e1994f21c', 11.50, 'Burger Menü mit Getränk', null, 2
);

INSERT INTO ffb.product
(
	id, foodcourt_id, price, display_name, symbol, minimal_warning
)
VALUES
(
	'e3855f3f-5594-453f-8fe4-42052268f294', '67b462a3-40c4-43a6-8373-a66e1994f21c', 10.50, 'Burger Menü', null, 2
);

INSERT INTO ffb.product
(
	id, foodcourt_id, price, display_name, symbol, minimal_warning
)
VALUES
(
	'1f620340-7af2-406b-90e0-17337a0a3130', '67b462a3-40c4-43a6-8373-a66e1994f21c', 5.50, 'Burger', null, 2
);

INSERT INTO ffb.product
(
	id, foodcourt_id, price, display_name, symbol, minimal_warning
)
VALUES
(
	'f7a207fe-574f-45c4-ab5c-80130abb9341', '67b462a3-40c4-43a6-8373-a66e1994f21c', 3.50, 'Pommes', null, 2
);
	
INSERT INTO ffb.product
(
	id, foodcourt_id, price, display_name, symbol, minimal_warning
)
VALUES
(
	'4bb76a82-e3c8-4927-a03d-81049372ea96', '67b462a3-40c4-43a6-8373-a66e1994f21c', 2.50, 'Cola', null, 2
);


INSERT INTO ffb.sub_product
(
	main_product_id, sub_product_id
)
VALUES
(
	'bf9a3924-d2ca-4aa7-a0ba-44770b081881', '1f620340-7af2-406b-90e0-17337a0a3130'
);

INSERT INTO ffb.sub_product
(
	main_product_id, sub_product_id
)
VALUES
(
	'bf9a3924-d2ca-4aa7-a0ba-44770b081881', 'f7a207fe-574f-45c4-ab5c-80130abb9341'
);


INSERT INTO ffb.sub_product
(
	main_product_id, sub_product_id
)
VALUES
(
	'bf9a3924-d2ca-4aa7-a0ba-44770b081881', '4bb76a82-e3c8-4927-a03d-81049372ea96'
);

INSERT INTO ffb.sub_product
(
	main_product_id, sub_product_id
)
VALUES
(
	'e3855f3f-5594-453f-8fe4-42052268f294', '1f620340-7af2-406b-90e0-17337a0a3130'
);

INSERT INTO ffb.sub_product
(
	main_product_id, sub_product_id
)
VALUES
(
	'e3855f3f-5594-453f-8fe4-42052268f294', 'f7a207fe-574f-45c4-ab5c-80130abb9341'
);
