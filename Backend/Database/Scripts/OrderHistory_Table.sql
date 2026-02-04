CREATE TABLE ffb.Order_History
(
	id UUID
	, order_Id UUID
	, status_Change_Time TimeStamp
	, old_Status ffb.ORDER_STATUS
	, new_Status ffb.ORDER_STATUS
	, PRIMARY KEY (id)
	,CONSTRAINT fk_Orders
		FOREIGN KEY (order_Id)
			REFERENCES ffb.Orders(id)
);