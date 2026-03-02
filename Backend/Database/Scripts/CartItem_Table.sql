CREATE TABLE ffb.Cart_Item
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