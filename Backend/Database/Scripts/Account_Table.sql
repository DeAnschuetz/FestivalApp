CREATE TABLE IF NOT EXISTS ffb.Account
(
	login_Nr UUID
	, password CHARACTER VARYING(60)
	, type ffb.ACCOUNT_TYPE
	, PRIMARY KEY (login_Nr)
);