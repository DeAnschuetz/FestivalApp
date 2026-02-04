CREATE TABLE IF NOT EXISTS ffb.Foodcourt
(
	id UUID
	,login_Nr UUID
	, display_Name CHARACTER VARYING
	, image BYTEA 
	, PRIMARY KEY (id)
	, CONSTRAINT fk_Account
		FOREIGN KEY (login_Nr)
			REFERENCES ffb.Account(login_Nr)
);

CREATE TABLE IF NOT EXISTS ffb.Foodcourt_Waiting_Time
(
	foodcourt_Id UUID
	, waiting_Time INTEGER
	, PRIMARY KEY (foodcourt_Id)
	, CONSTRAINT fk_Foodcourt
		FOREIGN KEY (foodcourt_Id)
			REFERENCES ffb.Foodcourt(id)
);