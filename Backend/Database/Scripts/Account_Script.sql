DROP TABLE IF EXISTS ffb.Account;
DROP TYPE IF EXISTS ffb.ACCOUNT_TYPE;
CREATE TYPE ffb.ACCOUNT_TYPE AS ENUM ('festivalAdmin', 'foodCourtWorker', 'fastivalGuest');

CREATE TABLE IF NOT EXISTS ffb.Account
(
	login_Nr UUID
	, password CHARACTER VARYING(60)
	, type ffb.ACCOUNT_TYPE
	, PRIMARY KEY (login_Nr)
);