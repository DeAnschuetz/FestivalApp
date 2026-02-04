CREATE TABLE ffb.Food_Order_Item
(
	id UUID
	, order_Id UUID
	, product_Id UUID
	, price NUMERIC
	, item_Count INTEGER
	, extra CHARACTER VARYING
	, PRIMARY KEY (id)
	, CONSTRAINT fk_Food_Order
		FOREIGN KEY (order_Id)
			REFERENCES ffb.Food_Order(id)
	,CONSTRAINT fk_Product
		FOREIGN KEY (product_Id)
			REFERENCES ffb.Product(id)
);