CREATE TABLE IF NOT EXISTS ffb.Credit
(
	login_Nr UUID
	, ammount NUMERIC
	, PRIMARY KEY (login_Nr)
	, CONSTRAINT fk_Account
		FOREIGN KEY (login_Nr)
			REFERENCES ffb.Account(login_Nr)
);

CREATE TABLE IF NOT EXISTS ffb.Credit_History
(
	id UUID
	, login_Nr UUID
	, old_Ammount NUMERIC
	, new_Ammount NUMERIC
	, change_Time TIMESTAMP
	, PRIMARY KEY(id)
	, CONSTRAINT fk_Account
		FOREIGN KEY (login_Nr)
			REFERENCES ffb.Account(login_Nr)
);