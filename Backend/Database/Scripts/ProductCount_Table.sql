CREATE TABLE ffb.Product_Count
(
	product_Id UUID
	, product_Count INTEGER
	, PRIMARY KEY (product_Id)
	, CONSTRAINT fk_Product
		FOREIGN KEY (product_Id)
			REFERENCES ffb.Produtc(id)
);