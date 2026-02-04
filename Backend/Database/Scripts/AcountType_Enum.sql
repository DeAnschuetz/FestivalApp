DROP TYPE IF EXISTS ffb.ACCOUNT_TYPE;
CREATE TYPE ffb.ACCOUNT_TYPE AS ENUM ('festivalAdmin', 'foodCourtWorker', 'fastivalGuest');
