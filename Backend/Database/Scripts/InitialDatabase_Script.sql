
---Account_Script
CREATE SCHEMA IF NOT EXISTS ffb;

DROP TABLE IF EXISTS ffb.account;
CREATE TABLE IF NOT EXISTS ffb.Account
(
	id UUID
	, login_nr CHARACTER VARYING(13) UNIQUE
	, password CHARACTER VARYING(60)
	, type CHARACTER VARYING(15) check ((type in ('FESTIVAL_ADMIN','FOOCOURT_WORKER','FESTIVAL_GUEST')))
	, PRIMARY KEY (id)
);

---Credit_Script
DROP TABLE IF EXISTS ffb.credit;
CREATE TABLE IF NOT EXISTS ffb.credit
(
	id UUID
	, account_id UUID UNIQUE
	, ammount DECIMAL
	, PRIMARY KEY (id)
	, CONSTRAINT fk_account
		FOREIGN KEY (account_id)
			REFERENCES ffb.account(id)
);

DROP TABLE IF EXISTS ffb.credit_history;
CREATE TABLE IF NOT EXISTS ffb.credit_history
(
	id UUID
	, account_id UUID
	, old_ammount DECIMAL
	, new_ammount DECIMAL
	, change_time TIMESTAMP
	, PRIMARY KEY(id)
	, CONSTRAINT fk_account
		FOREIGN KEY (account_Id)
			REFERENCES ffb.account(id)
	, CONSTRAINT fk_credit
		FOREIGN KEY (credit_id)
			REFERENCES ffb.credit(id)
);

---Notification_Script
DROP TABLE IF EXISTS ffb.notification;
CREATE TABLE ffb.notification
(
	id UUID
	, account_id UUID
	, notification_Type CHARACTER VARYING(15) check ((type in ('FESTIVAL_ADMIN','FOOCOURT_WORKER','FESTIVAL_GUEST')))
	, status CHARACTER VARYING(15) check ((type in ('FESTIVAL_ADMIN','FOOCOURT_WORKER','FESTIVAL_GUEST')))
	, notification_message CHARACTER VARYING
	, order_zime TIMESTAMP
	, pickup_zime TIMESTAMP
	, PRIMARY KEY (id)
	, CONSTRAINT fk_account
		FOREIGN KEY (account_id)
			REFERENCES ffb.account(id)
);


---Foodcourt_Script
DROP TABLE IF EXISTS ffb.foodcourt;
CREATE TABLE IF NOT EXISTS ffb.foodcourt
(
	id UUID
	,account_id UUID UNIQUE
	, display_Name CHARACTER VARYING
	, image BYTEA 
	, PRIMARY KEY (id)
	, CONSTRAINT fk_Account
		FOREIGN KEY (account_Id)
			REFERENCES ffb.Account(id)
);

CREATE TABLE IF NOT EXISTS ffb.Foodcourt_Waiting_Time
(
	id UUID
	, foodcourt_Id UUID UNIQUE
	, waiting_Time INTEGER
	, PRIMARY KEY (id)
	, CONSTRAINT fk_Foodcourt
		FOREIGN KEY (foodcourt_Id)
			REFERENCES ffb.Foodcourt(id)
);

---Product_Script
CREATE TABLE IF NOT EXISTS ffb.Product
(
	id UUID
	, foodcourt_Id UUID
	, price DECIMAL
	, display_Name CHARACTER VARYING
	, symbol BYTEA
	, minimal_Warning INTEGER
	, PRIMARY KEY(id)
	, CONSTRAINT fk_Foodcourt
		FOREIGN KEY (foodcourt_Id)
			REFERENCES ffb.Foodcourt(id)
);

CREATE TABLE IF NOT EXISTS ffb.Sub_Product
(
	main_Product_Id UUID
	, sub_Product_Id UUID
	, PRIMARY KEY (main_Product_Id, sub_Product_Id)
	, CONSTRAINT fk_Main_Product
		FOREIGN KEY (main_Product_Id)
			REFERENCES ffb.Product(id)
	, CONSTRAINT fk_Sub_Product
		FOREIGN KEY (sub_Product_Id)
			REFERENCES ffb.Product(id)
);

CREATE TABLE IF NOT EXISTS ffb.Product_Count
(
	product_Id UUID
	, product_Count INTEGER
	, PRIMARY KEY (product_Id)
	, CONSTRAINT fk_Product
		FOREIGN KEY (product_Id)
			REFERENCES ffb.Product(id)
);

---FoodOrder_Script
DROP TABLE IF EXISTS ffb.Food_Order;
DROP TABLE IF EXISTS ffb.Food_Order_History;
DROP TYPE IF EXISTS ffb.FOOD_ORDER_STATUS;
CREATE TYPE ffb.FOOD_ORDER_STATUS AS ENUM ('ordered', 'in_Progress', 'ready_for_Pickup', 'done', 'canceled');

CREATE TABLE IF NOT EXISTS ffb.Food_Order
(
	id UUID
	, account_Id UUID
	, foodcourt_Id UUID
	, status ffb.FOOD_ORDER_STATUS
	, has_Prio BOOLEAN
	, total DECIMAL
	, waiting_Time INT
	, is_hidden BOOLEAN
	, PRIMARY KEY (id)
	,CONSTRAINT fk_Account
		FOREIGN KEY (account_Id)
			REFERENCES ffb.Account(id)
	, CONSTRAINT fk_Foodcourt
		FOREIGN KEY (foodcourt_Id)
			REFERENCES ffb.Foodcourt(id)
);

CREATE TABLE IF NOT EXISTS ffb.Food_Order_Item
(
	id UUID
	, food_order_Id UUID
	, product_Id UUID
	, price DECIMAL
	, item_Count INTEGER
	, extra CHARACTER VARYING
	, PRIMARY KEY (id)
	, CONSTRAINT fk_Food_Order
		FOREIGN KEY (food_order_Id)
			REFERENCES ffb.Food_Order(id)
	,CONSTRAINT fk_Product
		FOREIGN KEY (product_Id)
			REFERENCES ffb.Product(id)
);

CREATE TABLE IF NOT EXISTS ffb.Food_Order_History
(
	id UUID
	, food_order_id UUID
	, status_Change_Time TimeStamp
	, old_Status ffb.FOOD_ORDER_STATUS
	, new_Status ffb.FOOD_ORDER_STATUS
	, PRIMARY KEY (id)
	,CONSTRAINT fk_Food_Order
		FOREIGN KEY (order_Id)
			REFERENCES ffb.Food_Order(id)
);

---Cart_Script
CREATE TABLE IF NOT EXISTS ffb.Cart
(
	id UUID
	, account_Id UUID
	, has_Prio BOOLEAN
	, total DECIMAL
	, PRIMARY KEY (id)
	, CONSTRAINT fk_Account
		FOREIGN KEY (account_Id)
			REFERENCES ffb.Account(id)
);

CREATE TABLE IF NOT EXISTS ffb.Cart_Item
(
	id UUID
	, cart_Id UUID
	, product_Id UUID
	, price DECIMAL
	, item_Count INTEGER
	, extra CHARACTER VARYING
	, PRIMARY KEY (id)
	, CONSTRAINT fk_Cart
		FOREIGN KEY (cart_Id)
			REFERENCES ffb.Cart(id)
	,CONSTRAINT fk_Product
		FOREIGN KEY (product_Id)
			REFERENCES ffb.Product(id)
);

CREATE VIEW  ffb.v_sub_product
AS 
(
	SELECT prod.id AS main_product_id, subProd.id AS id, prod.foodcourt_id, subProd.price, subProd.display_name, subProd.symbol, subProd.minimal_warning
	FROM ffb.product AS prod
	JOIN ffb.sub_product AS con ON prod.id = con.main_product_id
	JOIN ffb.product AS subProd ON subProd.id = con.sub_product_id
);