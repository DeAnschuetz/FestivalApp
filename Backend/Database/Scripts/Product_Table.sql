CREATE TABLE ffb.Product
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