CREATE TABLE ffb.Cart
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