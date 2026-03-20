CREATE SCHEMA IF NOT EXISTS ffb;



DROP TYPE IF EXISTS ffb.ACCOUNT_TYPE;
CREATE TYPE ffb.ACCOUNT_TYPE AS ENUM ('festivalAdmin', 'foodCourtWorker', 'fastivalGuest');

DROP TYPE IF EXISTS ffb.ORDER_STATUS;
CREATE TYPE ffb.ORDER_STATUS AS ENUM ('ordered', 'in_Progress', 'ready_for_Pickup', 'done', 'canceled');

DROP TYPE IF EXISTS ffb.FOOD_ORDER_STATUS;
CREATE TYPE ffb.FOOD_ORDER_STATUS AS ENUM ('ordered', 'in_Progress', 'ready_for_Pickup', 'done', 'canceled');

DROP TYPE IF EXISTS ffb.NOTIFICATION_TYPE;
CREATE TYPE ffb.NOTIFICATION_TYPE AS ENUM ('ordered', 'ready', 'canceled');

DROP TYPE IF EXISTS ffb.NOTIFICATION_STATUS_TYPE;
CREATE TYPE ffb.NOTIFICATION_STATUS_TYPE AS ENUM ('new', 'read', 'removed');



DROP TABLE IF EXISTS ffb.Account CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Account
(
	login_Nr UUID,
	password CHARACTER VARYING(60),
	type ffb.ACCOUNT_TYPE,
	PRIMARY KEY (login_Nr)
);

DROP TABLE IF EXISTS ffb.Product CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Product
(
	id UUID,
	foodcourt_Id UUID,
	price NUMERIC,
	display_Name CHARACTER VARYING,
	symbol BYTEA,
	minimal_Warning INTEGER,
	PRIMARY KEY(id)
);

DROP TABLE IF EXISTS ffb.Credit CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Credit
(
	login_Nr UUID,
	ammount NUMERIC,
	PRIMARY KEY (login_Nr),
	CONSTRAINT fk_Account FOREIGN KEY (login_Nr) REFERENCES ffb.Account(login_Nr)
);

DROP TABLE IF EXISTS ffb.Credit_History CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Credit_History
(
	id UUID,
	login_Nr UUID,
	old_Ammount NUMERIC,
	new_Ammount NUMERIC,
	change_Time TIMESTAMP,
	PRIMARY KEY(id),
	CONSTRAINT fk_Account FOREIGN KEY (login_Nr) REFERENCES ffb.Account(login_Nr)
);

DROP TABLE IF EXISTS ffb.Foodcourt CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Foodcourt
(
	id UUID,
	login_Nr UUID,
	display_Name CHARACTER VARYING,
	image BYTEA,
	PRIMARY KEY(id),
	CONSTRAINT fk_Account FOREIGN KEY (login_Nr) REFERENCES ffb.Account(login_Nr)
);

DROP TABLE IF EXISTS ffb.Foodcourt_Waiting_Time CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Foodcourt_Waiting_Time
(
	foodcourt_Id UUID,
	waiting_Time INTEGER,
	PRIMARY KEY(foodcourt_Id),
	CONSTRAINT fk_Foodcourt FOREIGN KEY (foodcourt_Id) REFERENCES ffb.Foodcourt(id)
);

DROP TABLE IF EXISTS ffb.Cart CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Cart
(
	id UUID,
	login_Nr UUID,
	has_Prio BOOLEAN,
	total NUMERIC,
	PRIMARY KEY(id),
	CONSTRAINT fk_Account FOREIGN KEY (login_Nr) REFERENCES ffb.Account(login_Nr)
);

DROP TABLE IF EXISTS ffb.Cart_Item CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Cart_Item
(
	id UUID,
	cart_Id UUID,
	product_Id UUID,
	price NUMERIC,
	item_Count INTEGER,
	extra CHARACTER VARYING,
	PRIMARY KEY(id),
	CONSTRAINT fk_Cart FOREIGN KEY (cart_Id) REFERENCES ffb.Cart(id),
	CONSTRAINT fk_Product FOREIGN KEY (product_Id) REFERENCES ffb.Product(id)
);

DROP TABLE IF EXISTS ffb.Sub_Product CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Sub_Product
(
	main_Product_Id UUID,
	sub_Product_Id UUID,
	PRIMARY KEY (main_Product_Id, sub_Product_Id),
	CONSTRAINT fk_Main_Product FOREIGN KEY (main_Product_Id) REFERENCES ffb.Product(id),
	CONSTRAINT fk_Sub_Product FOREIGN KEY (sub_Product_Id) REFERENCES ffb.Product(id)
);

DROP TABLE IF EXISTS ffb.Product_Count CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Product_Count
(
	product_Id UUID,
	product_Count INTEGER,
	PRIMARY KEY(product_Id),
	CONSTRAINT fk_Product FOREIGN KEY (product_Id) REFERENCES ffb.Product(id)
);

DROP TABLE IF EXISTS ffb.Food_Order CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Food_Order
(
	id UUID,
	login_Nr UUID,
	foodcourt_Id UUID,
	status ffb.ORDER_STATUS,
	has_Prio BOOLEAN,
	total NUMERIC,
	waiting_Time TIMESTAMP,
	is_hidden BOOLEAN,
	PRIMARY KEY(id),
	CONSTRAINT fk_Account FOREIGN KEY (login_Nr) REFERENCES ffb.Account(login_Nr),
	CONSTRAINT fk_Foodcourt FOREIGN KEY (foodcourt_Id) REFERENCES ffb.Foodcourt(id)
);

DROP TABLE IF EXISTS ffb.Food_Order_Item CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Food_Order_Item
(
	id UUID,
	order_Id UUID,
	product_Id UUID,
	price NUMERIC,
	item_Count INTEGER,
	extra CHARACTER VARYING,
	PRIMARY KEY(id),
	CONSTRAINT fk_Food_Order FOREIGN KEY (order_Id) REFERENCES ffb.Food_Order(id),
	CONSTRAINT fk_Product FOREIGN KEY (product_Id) REFERENCES ffb.Product(id)
);

DROP TABLE IF EXISTS ffb.Food_Order_History CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Food_Order_History
(
	id UUID,
	order_Id UUID,
	status_Change_Time TIMESTAMP,
	old_Status ffb.ORDER_STATUS,
	new_Status ffb.ORDER_STATUS,
	PRIMARY KEY(id),
	CONSTRAINT fk_Food_Order FOREIGN KEY (order_Id) REFERENCES ffb.Food_Order(id)
);

DROP TABLE IF EXISTS ffb.Notification CASCADE;
CREATE TABLE IF NOT EXISTS ffb.Notification
(
	id UUID,
	login_Nr UUID,
	notification_Type ffb.NOTIFICATION_TYPE,
	status ffb.NOTIFICATION_STATUS_TYPE,
	notification_Message CHARACTER VARYING,
	order_Time TIMESTAMP,
	pickup_Time TIMESTAMP,
	PRIMARY KEY(id),
	CONSTRAINT fk_Account FOREIGN KEY (login_Nr) REFERENCES ffb.Account(login_Nr)
);



CREATE VIEW ffb.v_sub_product AS
SELECT 
	prod.id AS main_product_id, 
	subProd.id AS id, 
	prod.foodcourt_id, 
	subProd.price, 
	subProd.display_name, 
	subProd.symbol, 
	subProd.minimal_warning
FROM ffb.product AS prod
JOIN ffb.sub_product AS con ON prod.id = con.main_product_id
JOIN ffb.product AS subProd ON subProd.id = con.sub_product_id;



INSERT INTO ffb.Account(login_Nr, password, type)
VALUES ('6767b5c9-f58c-40cd-be15-6427648ca543', '123456789', 'foodCourtWorker');

INSERT INTO ffb.Foodcourt(id, login_Nr, display_Name, image)
VALUES ('67b462a3-40c4-43a6-8373-a66e1994f21c', '6767b5c9-f58c-40cd-be15-6427648ca543', 'Burger Place', null);

INSERT INTO ffb.Product(id, foodcourt_Id, price, display_Name, symbol, minimal_Warning)
VALUES 
('bf9a3924-d2ca-4aa7-a0ba-44770b081881','67b462a3-40c4-43a6-8373-a66e1994f21c',11.50,'Burger Menü mit Getränk',null,2),
('e3855f3f-5594-453f-8fe4-42052268f294','67b462a3-40c4-43a6-8373-a66e1994f21c',10.50,'Burger Menü',null,2),
('1f620340-7af2-406b-90e0-17337a0a3130','67b462a3-40c4-43a6-8373-a66e1994f21c',5.50,'Burger',null,2),
('f7a207fe-574f-45c4-ab5c-80130abb9341','67b462a3-40c4-43a6-8373-a66e1994f21c',3.50,'Pommes',null,2),
('4bb76a82-e3c8-4927-a03d-81049372ea96','67b462a3-40c4-43a6-8373-a66e1994f21c',2.50,'Cola',null,2);

INSERT INTO ffb.Sub_Product(main_Product_Id, sub_Product_Id)
VALUES
('bf9a3924-d2ca-4aa7-a0ba-44770b081881','1f620340-7af2-406b-90e0-17337a0a3130'),
('bf9a3924-d2ca-4aa7-a0ba-44770b081881','f7a207fe-574f-45c4-ab5c-80130abb9341'),
('bf9a3924-d2ca-4aa7-a0ba-44770b081881','4bb76a82-e3c8-4927-a03d-81049372ea96'),
('e3855f3f-5594-453f-8fe4-42052268f294','1f620340-7af2-406b-90e0-17337a0a3130'),
('e3855f3f-5594-453f-8fe4-42052268f294','f7a207fe-574f-45c4-ab5c-80130abb9341');