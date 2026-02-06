CREATE TABLE ffb.Foodcourt_Waiting_Time
(
	foodcourt_Id UUID
	, waiting_Time INTEGER
	, PRIMARY KEY (foodcourt_Id)
	, CONSTRAINT fk_Foodcourt
		FOREIGN KEY (foodcourt_Id)
			REFERENCES ffb.Foodcourt(id)
)