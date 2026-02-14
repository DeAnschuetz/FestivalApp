SELECT
	COALESCE(subprod.id, prod.id) AS id
	, COALESCE(subprod.foodcourt_id, prod.foodcourt_id) AS foodcourt_id
	, COALESCE(subprod.price, prod.price) AS price
	, COALESCE(subprod.display_name, prod.display_name) AS display_name
	, COALESCE(subprod.symbol, prod.symbol) AS symbol
FROM ffb.product AS prod
LEFT JOIN ffb.v_sub_product AS subprod ON prod.id = subprod.main_product_id
WHERE prod.id = '1f620340-7af2-406b-90e0-17337a0a3130' OR prod.id = 'bf9a3924-d2ca-4aa7-a0ba-44770b081881';