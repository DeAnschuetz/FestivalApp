DROP TABLE IF EXISTS ffb.Notification;
DROP TYPE IF EXISTS ffb.NOTIFICATION_TYPE;
CREATE TYPE ffb.NOTIFICATION_TYPE AS ENUM ('ordered', 'ready', 'canceled');
DROP TYPE IF EXISTS ffb.NOTIFICATION_STATUS_TYPE;
CREATE TYPE ffb.NOTIFICATION_STATUS_TYPE AS ENUM ('new', 'read', 'removed');

CREATE TABLE ffb.Notification
(
	id UUID
	, login_Nr UUID
	, notification_Type ffb.NOTIFICATION_TYPE
	, status ffb.NOTIFICATION_STATUS_TYPE
	, notification_Message CHARACTER VARYING
	, order_Time TIMESTAMP
	, pickup_Time TIMESTAMP
	, PRIMARY KEY (id)
	, CONSTRAINT fk_Account
		FOREIGN KEY (login_Nr)
			REFERENCES ffb.Account(login_Nr)
);
