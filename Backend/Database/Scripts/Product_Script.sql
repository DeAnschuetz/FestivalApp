CREATE TABLE IF NOT EXISTS ffb.Product
(
	id UUID
	, foodcourt_Id UUID
	, price NUMERIC
	, display_Name CHARACTER VARYING
	, symbol BYTEA
	, minimal_Warning INTEGER
	, PRIMARY KEY(id)
	, CONSTRAINT fk_Foodcourt
		FOREIGN KEY (foodcourt_Id)
			REFERENCES ffb.Foodcourt(id)
);

CREATE TABLE IF NOT EXISTS ffb.Sub_Product
(
	main_Product_Id UUID
	, sub_Product_Id UUID
	, PRIMARY KEY (main_Product_Id, sub_Product_Id)
	, CONSTRAINT fk_Main_Product
		FOREIGN KEY (main_Product_Id)
			REFERENCES ffb.Product(id)
	, CONSTRAINT fk_Sub_Product
		FOREIGN KEY (sub_Product_Id)
			REFERENCES ffb.Product(id)
);

CREATE TABLE IF NOT EXISTS ffb.Product_Count
(
	product_Id UUID
	, product_Count INTEGER
	, PRIMARY KEY (product_Id)
	, CONSTRAINT fk_Product
		FOREIGN KEY (product_Id)
			REFERENCES ffb.Product(id)
);