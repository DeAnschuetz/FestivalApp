CREATE TABLE ffb.Foodcourt
(
	id UUID
	,login_Nr UUID
	, display_Name CHARACTER VARYING
	, image BYTEA 
	, PRIMARY KEY (id)
	, CONSTRAINT fk_Account
		FOREIGN KEY (login_Nr)
			REFERENCES Account(login_Nr)
);