CREATE TABLE ffb.Food_Order
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
