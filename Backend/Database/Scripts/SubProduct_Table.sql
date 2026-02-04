CREATE TABLE ffb.Sub_Product
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