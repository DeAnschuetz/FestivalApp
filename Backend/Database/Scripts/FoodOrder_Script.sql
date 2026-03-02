DROP TABLE IF EXISTS ffb.Food_Order;
DROP TABLE IF EXISTS ffb.Food_Order_History;
DROP TYPE IF EXISTS ffb.ORDER_STATUS;
CREATE TYPE ffb.ORDER_STATUS AS ENUM ('ordered', 'in_Progress', 'ready_for_Pickup', 'done', 'canceled');

CREATE TABLE IF NOT EXISTS ffb.Food_Order
(
	id UUID
	, login_Nr UUID
	, foodcourt_Id UUID
	, status ffb.ORDER_STATUS
	, has_Prio BOOLEAN
	, total NUMERIC
	, waiting_Time TIMESTAMP
	, is_hidden BOOLEAN
	, PRIMARY KEY (id)
	,CONSTRAINT fk_Account
		FOREIGN KEY (login_Nr)
			REFERENCES ffb.Account(login_Nr)
	, CONSTRAINT fk_Foodcourt
		FOREIGN KEY (foodcourt_Id)
			REFERENCES ffb.Foodcourt(id)
);

CREATE TABLE IF NOT EXISTS ffb.Food_Order_Item
(
	id UUID
	, order_Id UUID
	, product_Id UUID
	, price NUMERIC
	, item_Count INTEGER
	, extra CHARACTER VARYING
	, PRIMARY KEY (id)
	, CONSTRAINT fk_Food_Order
		FOREIGN KEY (order_Id)
			REFERENCES ffb.Food_Order(id)
	,CONSTRAINT fk_Product
		FOREIGN KEY (product_Id)
			REFERENCES ffb.Product(id)
);

CREATE TABLE IF NOT EXISTS ffb.Food_Order_History
(
	id UUID
	, order_Id UUID
	, status_Change_Time TimeStamp
	, old_Status ffb.ORDER_STATUS
	, new_Status ffb.ORDER_STATUS
	, PRIMARY KEY (id)
	,CONSTRAINT fk_Food_Order
		FOREIGN KEY (order_Id)
			REFERENCES ffb.Food_Order(id)
);