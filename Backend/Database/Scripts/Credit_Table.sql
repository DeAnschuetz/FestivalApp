CREATE TABLE IF NOT EXISTS ffb.Credit
(
	login_Nr UUID
	, ammount NUMERIC
	, PRIMARY KEY (login_Nr)
	, CONSTRAINT fk_Account
		FOREIGN KEY (login_Nr)
			REFERENCES ffb.Account(login_Nr)
);