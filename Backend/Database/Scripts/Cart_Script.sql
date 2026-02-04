CREATE TABLE IF NOT EXISTS ffb.Cart
(
	id UUID
	, login_Nr UUID
	, has_Prio BOOLEAN
	, total NUMERIC
	, PRIMARY KEY (id)
	, CONSTRAINT fk_Account
		FOREIGN KEY (login_Nr)
			REFERENCES ffb.Account(login_Nr)
);

CREATE TABLE IF NOT EXISTS ffb.Cart_Item
(
	id UUID
	, cart_Id UUID
	, product_Id UUID
	, price NUMERIC
	, item_Count INTEGER
	, extra CHARACTER VARYING
	, PRIMARY KEY (id)
	, CONSTRAINT fk_Cart
		FOREIGN KEY (cart_Id)
			REFERENCES ffb.Cart(id)
	,CONSTRAINT fk_Product
		FOREIGN KEY (product_Id)
			REFERENCES ffb.Product(id)
);