--
-- PostgreSQL database dump
--

\restrict W6vA6DbPaSg4xpS4965itzJadQnfMK1f1O7an6IpY72vGRPD2BV0gIHFJ0HQQwH

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

-- Started on 2026-03-22 17:28:17

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5157 (class 0 OID 24365)
-- Dependencies: 236
-- Data for Name: ticket; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.ticket (id, login_nr) FROM stdin;
42a628e4-3bce-447b-b177-5f0b1cac60db	A-000-000-000
65fd0b70-3aaa-4552-8438-9e9d052ca336	V-000-000-001
dde309c9-4f81-41bf-b81b-0dcc2d1e5df3	V-000-000-002
63301b3c-51fc-4de4-8ae1-1be94d7fa05e	V-000-000-003
93a71157-a6be-4918-b3e7-fdbae7ccd17a	V-000-000-004
31c3d052-3b29-44ec-81d2-528d2144dd8d	V-000-000-005
25aa6df4-d48a-4d04-bfbc-cb001c64c0f1	V-000-000-006
d2034150-a1ed-4b4b-b794-2472354e98b2	V-000-000-007
4936a234-59c1-4f05-a9b5-9517aca4c166	V-000-000-008
35d451ab-5622-44f7-958c-8a22b34e914d	V-000-000-009
2736c79d-6147-47e3-887e-40811cd1f91b	V-000-000-010
f285a36c-d189-43f1-b235-846d2ecc804e	F-000-000-001
47dc5c33-101d-4812-91e9-b81c966dcba9	F-000-000-002
558951ca-3997-4c19-a43e-1771946f20d7	F-000-000-003
a49900d1-32a7-4272-b4cc-93071a157236	F-000-000-004
5c9a88be-d7a7-49ae-936d-e88b276c2ea5	F-000-000-005
\.


--
-- TOC entry 5143 (class 0 OID 24245)
-- Dependencies: 222
-- Data for Name: account; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.account (id, password, type, ticket_id) FROM stdin;
fe099c8c-06da-48b9-9c36-7df26322fe94	$2a$12$f4JQ6qiSZaHETrPXrwVFq.RTafWQuN7EKb9Y39oAXnonhfN1xn9eG	ADMIN	42a628e4-3bce-447b-b177-5f0b1cac60db
e5a6778b-8576-4b4c-a4a3-97c5a3ac2cf0	$2a$12$adrCpO6iR3BWIvfKmHj87ePzrSXrk5N0NGZQqz9dU1BsywZ2xvDzO	FOOD_COURT_WORKER	f285a36c-d189-43f1-b235-846d2ecc804e
50c3ba6b-903e-4d00-81dd-2314c3ed478f	$2a$12$sdXsDNW8NL4DewRRI43GguU6U9Kg8Ul/0S41AAsLtiSWzC/gHD6CS	FOOD_COURT_WORKER	47dc5c33-101d-4812-91e9-b81c966dcba9
484b61d1-064e-4f69-be9e-9cacd5a773f6	$2a$12$uT1P2XY1ZCTQUkcZA97jLObU1JjG3Ape/oyyZPeIYUc3dxCPmIAq2	GUEST	65fd0b70-3aaa-4552-8438-9e9d052ca336
dcddd091-cec9-4710-8cf2-c885d1b89e1f	$2a$12$49fFHnrujRHU5voP2HzsVeAboIiuGr/9TvvKhg0laUhPEysIAReQm	GUEST	dde309c9-4f81-41bf-b81b-0dcc2d1e5df3
\.


--
-- TOC entry 5144 (class 0 OID 24255)
-- Dependencies: 223
-- Data for Name: cart; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.cart (id, has_prio, total, account_id) FROM stdin;
d261a674-bc31-461b-8f13-1e11f91f6939	f	0.00	484b61d1-064e-4f69-be9e-9cacd5a773f6
74321b9b-0432-4702-a559-7fe92c18b25d	f	0.00	dcddd091-cec9-4710-8cf2-c885d1b89e1f
\.


--
-- TOC entry 5148 (class 0 OID 24284)
-- Dependencies: 227
-- Data for Name: food_court; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.food_court (id, display_name, image, account_id) FROM stdin;
a6caa51c-52e0-4ea8-80bc-bcf3d2c03efc	Burger Palace	24487	e5a6778b-8576-4b4c-a4a3-97c5a3ac2cf0
e9b30a36-d946-4295-b934-9aec9014c8c1	Pizza Palace	24488	50c3ba6b-903e-4d00-81dd-2314c3ed478f
\.


--
-- TOC entry 5155 (class 0 OID 24349)
-- Dependencies: 234
-- Data for Name: product; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.product (id, display_name, minimal_warning, price, symbol_identifier, food_court_id) FROM stdin;
8d3cf1a0-7b8c-450f-8cdd-6714f672027c	Pizza Margherita Menü mit Fanta	10	12.00	TEST	e9b30a36-d946-4295-b934-9aec9014c8c1
a7dfc4a1-59f5-41d1-9cdd-c1e3ce8f0be6	Pizza Margherita Menü	10	11.00	TEST	e9b30a36-d946-4295-b934-9aec9014c8c1
17c5680e-22d5-4647-b5bb-b4a586f6c6d2	Pizza Margherita	10	7.50	TEST	e9b30a36-d946-4295-b934-9aec9014c8c1
8df2fa53-75b6-42c7-8798-c4157e756c41	Fanta	10	1.50	TEST	e9b30a36-d946-4295-b934-9aec9014c8c1
fe099c8c-06da-48b9-9c36-7df26322fe94	Antipasti	10	4.00	TEST	e9b30a36-d946-4295-b934-9aec9014c8c1
65de6f9f-c0b4-4038-896d-3bb80aa52434	Burger	10	7.50	fa fa-burger	a6caa51c-52e0-4ea8-80bc-bcf3d2c03efc
ea6e8266-b455-4136-9415-29ef8de05a15	Pommes	10	4.00	fa fa-bacon	a6caa51c-52e0-4ea8-80bc-bcf3d2c03efc
b9b0cc8f-53d3-40f7-9afe-d3c491ce2e75	Cola	10	7.50	fa fa-glass-water	a6caa51c-52e0-4ea8-80bc-bcf3d2c03efc
88091439-2cc5-4e25-af0b-27fe6d393563	Burger Menü mit Cola	10	12.00	fa fa-utensils	a6caa51c-52e0-4ea8-80bc-bcf3d2c03efc
dbcb35ab-0e7e-4af0-8517-c2ab45897f18	Burger Menü	10	11.00	fa fa-utensils	a6caa51c-52e0-4ea8-80bc-bcf3d2c03efc
\.


--
-- TOC entry 5145 (class 0 OID 24262)
-- Dependencies: 224
-- Data for Name: cart_item; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.cart_item (id, extra, item_count, price, cart_id, product_id) FROM stdin;
\.


--
-- TOC entry 5146 (class 0 OID 24270)
-- Dependencies: 225
-- Data for Name: credit; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.credit (id, amount, account_id) FROM stdin;
ecc86ef0-04e2-47dc-83ce-c60f81687c21	1892.00	dcddd091-cec9-4710-8cf2-c885d1b89e1f
640f39e7-82bf-4d15-9ba3-ebabb948a1db	10961.00	484b61d1-064e-4f69-be9e-9cacd5a773f6
\.


--
-- TOC entry 5147 (class 0 OID 24277)
-- Dependencies: 226
-- Data for Name: credit_history; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.credit_history (id, change_time, new_amount, old_amount, credit_id) FROM stdin;
b6914e6b-67e3-413c-af80-a312115cc551	2026-03-22 14:05:55.309935	1000.00	0.00	640f39e7-82bf-4d15-9ba3-ebabb948a1db
b894c355-7524-4aa7-a7b4-c97599a4896d	2026-03-22 14:05:55.582871	1000.00	0.00	ecc86ef0-04e2-47dc-83ce-c60f81687c21
709f2e5e-0187-4cf3-a007-61d4e4356582	2026-03-22 14:05:55.85873	11000.00	1000.00	640f39e7-82bf-4d15-9ba3-ebabb948a1db
3e7d3f69-ce34-4bee-b110-4c244d61a57c	2026-03-22 14:05:55.888389	2000.00	1000.00	ecc86ef0-04e2-47dc-83ce-c60f81687c21
9c54a617-348a-4a78-be90-fc1979b7d1ed	2026-03-22 14:05:56.111947	10941.00	11000.00	640f39e7-82bf-4d15-9ba3-ebabb948a1db
c3e0dd21-0bad-4961-bcae-fb52d9355a9d	2026-03-22 14:05:56.161131	1951.00	2000.00	ecc86ef0-04e2-47dc-83ce-c60f81687c21
813e97b9-ded1-46d3-aafb-c040ba0fab69	2026-03-22 14:05:56.17736	1892.00	1951.00	ecc86ef0-04e2-47dc-83ce-c60f81687c21
3aa951f0-2935-4718-a7bd-0fad6c91d64b	2026-03-22 14:11:02.395877	10951.00	10941.00	640f39e7-82bf-4d15-9ba3-ebabb948a1db
b22545bb-5e67-4adf-9f67-2936145caf1b	2026-03-22 14:55:13.492885	10961.00	10951.00	640f39e7-82bf-4d15-9ba3-ebabb948a1db
\.


--
-- TOC entry 5149 (class 0 OID 24292)
-- Dependencies: 228
-- Data for Name: food_court_waiting_time; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.food_court_waiting_time (id, waiting_time, food_court_id) FROM stdin;
f9b2a5d4-afc8-4c25-9d8d-25610d8c9953	0	e9b30a36-d946-4295-b934-9aec9014c8c1
0de726ba-8a44-4f4a-882e-8069d9f0712c	0	a6caa51c-52e0-4ea8-80bc-bcf3d2c03efc
\.


--
-- TOC entry 5150 (class 0 OID 24302)
-- Dependencies: 229
-- Data for Name: food_order; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.food_order (id, has_prio, is_hidden, order_time, status, total, waiting_time, account_id, food_court_id, shared_account_id) FROM stdin;
d23aab68-6342-4511-a208-ccf56c6a98a2	f	f	2026-03-22 14:05:56.167654	READY_FOR_PICKUP	59.00	0	dcddd091-cec9-4710-8cf2-c885d1b89e1f	a6caa51c-52e0-4ea8-80bc-bcf3d2c03efc	\N
e3405920-c6d1-441f-a991-caed36c4d163	f	f	2026-03-22 14:05:56.10843	READY_FOR_PICKUP	59.00	0	484b61d1-064e-4f69-be9e-9cacd5a773f6	a6caa51c-52e0-4ea8-80bc-bcf3d2c03efc	\N
a5a77d4d-b0ed-4c92-9962-fda488cd54b9	f	f	2026-03-22 14:05:56.158257	READY_FOR_PICKUP	49.00	0	dcddd091-cec9-4710-8cf2-c885d1b89e1f	e9b30a36-d946-4295-b934-9aec9014c8c1	\N
\.


--
-- TOC entry 5151 (class 0 OID 24311)
-- Dependencies: 230
-- Data for Name: food_order_history; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.food_order_history (id, new_status, old_status, status_change_time, food_order_id) FROM stdin;
5d315140-f20d-47d8-b4b0-1b4dcfe073ae	ORDERED	\N	2026-03-22 14:05:56.10843	e3405920-c6d1-441f-a991-caed36c4d163
9c08bf98-90b9-4a55-8a43-d66b47588d8d	ORDERED	\N	2026-03-22 14:05:56.158257	a5a77d4d-b0ed-4c92-9962-fda488cd54b9
2a033a04-162f-41f6-8922-2c0e824efa18	ORDERED	\N	2026-03-22 14:05:56.167654	d23aab68-6342-4511-a208-ccf56c6a98a2
61200dec-caef-4065-bb7f-70fc9aff7deb	IN_PROGRESS	ORDERED	2026-03-22 15:04:13.032809	d23aab68-6342-4511-a208-ccf56c6a98a2
c61601dc-cc08-4403-b272-e38771064b2d	IN_PROGRESS	ORDERED	2026-03-22 15:04:13.035482	e3405920-c6d1-441f-a991-caed36c4d163
8bb02d8d-c6ce-455e-9ee8-ab50b0d3b129	IN_PROGRESS	ORDERED	2026-03-22 15:04:13.111202	a5a77d4d-b0ed-4c92-9962-fda488cd54b9
bceec825-957e-4bdc-8eed-1f838a0ecf8a	READY_FOR_PICKUP	IN_PROGRESS	2026-03-22 15:18:48.588794	d23aab68-6342-4511-a208-ccf56c6a98a2
c45b897b-e03a-430e-ac78-e4d1f0606c57	CANCELED	READY_FOR_PICKUP	2026-03-22 16:06:46.271325	d23aab68-6342-4511-a208-ccf56c6a98a2
feb61ff0-c0f2-42fd-b1a1-d1c12154d66e	IN_PROGRESS	CANCELED	2026-03-22 16:07:13.400013	d23aab68-6342-4511-a208-ccf56c6a98a2
e2a1771b-55af-4ea2-b42c-f0083a02a895	READY_FOR_PICKUP	IN_PROGRESS	2026-03-22 16:23:29.090899	d23aab68-6342-4511-a208-ccf56c6a98a2
ada450e2-c0b3-446f-ac50-0aed57dd7e09	READY_FOR_PICKUP	IN_PROGRESS	2026-03-22 16:23:29.108537	e3405920-c6d1-441f-a991-caed36c4d163
906e3892-1c47-4863-899a-6529a0252144	READY_FOR_PICKUP	IN_PROGRESS	2026-03-22 16:23:29.233763	a5a77d4d-b0ed-4c92-9962-fda488cd54b9
\.


--
-- TOC entry 5152 (class 0 OID 24322)
-- Dependencies: 231
-- Data for Name: food_order_item; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.food_order_item (id, extra, item_count, price, food_order_id, product_id) FROM stdin;
d37b4fd8-ec36-4527-bb39-772b12494843		2	11.00	e3405920-c6d1-441f-a991-caed36c4d163	dbcb35ab-0e7e-4af0-8517-c2ab45897f18
cf83c8e4-647d-41e2-95e0-a114e2a6d819	mit extra Käse	1	13.00	e3405920-c6d1-441f-a991-caed36c4d163	dbcb35ab-0e7e-4af0-8517-c2ab45897f18
34ad47f4-2b48-4f86-ba87-5e3cafc87fa4		2	12.00	e3405920-c6d1-441f-a991-caed36c4d163	88091439-2cc5-4e25-af0b-27fe6d393563
be0760b9-347a-4048-9092-cf5b14f66567		2	7.50	a5a77d4d-b0ed-4c92-9962-fda488cd54b9	17c5680e-22d5-4647-b5bb-b4a586f6c6d2
2858af9b-0e7c-4e10-a922-e7b9d721f180	mit extra Knoblauch	2	13.00	a5a77d4d-b0ed-4c92-9962-fda488cd54b9	a7dfc4a1-59f5-41d1-9cdd-c1e3ce8f0be6
0498628a-8c16-4a6d-ae4c-a4029b439176		2	4.00	a5a77d4d-b0ed-4c92-9962-fda488cd54b9	fe099c8c-06da-48b9-9c36-7df26322fe94
d44440d6-6fa5-42b7-9b4d-1668f77bcc7e		2	11.00	d23aab68-6342-4511-a208-ccf56c6a98a2	dbcb35ab-0e7e-4af0-8517-c2ab45897f18
1c304252-42bc-4762-87d2-d0bd1485e08c	mit extra Käse	1	13.00	d23aab68-6342-4511-a208-ccf56c6a98a2	dbcb35ab-0e7e-4af0-8517-c2ab45897f18
83b8f21b-12b0-4e49-9799-1b73a78f33f5		2	12.00	d23aab68-6342-4511-a208-ccf56c6a98a2	88091439-2cc5-4e25-af0b-27fe6d393563
\.


--
-- TOC entry 5153 (class 0 OID 24330)
-- Dependencies: 232
-- Data for Name: food_order_notification; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.food_order_notification (id, creation_time, message, pickup_time, status, type, account_id, order_id) FROM stdin;
105d2e82-4a36-4020-bc73-88598a17f07d	2026-03-22 14:05:56.10843	Order {e3405920-c6d1-441f-a991-caed36c4d163} was placed	2026-03-22 14:05:56.10843	NEW	ORDERED	484b61d1-064e-4f69-be9e-9cacd5a773f6	e3405920-c6d1-441f-a991-caed36c4d163
41c98afa-9f0d-4dc2-9f1a-98a49a40822d	2026-03-22 14:05:56.158257	Order {a5a77d4d-b0ed-4c92-9962-fda488cd54b9} was placed	2026-03-22 14:05:56.158257	NEW	ORDERED	dcddd091-cec9-4710-8cf2-c885d1b89e1f	a5a77d4d-b0ed-4c92-9962-fda488cd54b9
33acdba8-bbea-401e-b72e-9c2a0034e6dd	2026-03-22 14:05:56.167654	Order {d23aab68-6342-4511-a208-ccf56c6a98a2} was placed	2026-03-22 14:05:56.167654	NEW	ORDERED	dcddd091-cec9-4710-8cf2-c885d1b89e1f	d23aab68-6342-4511-a208-ccf56c6a98a2
ffb3d2b1-6870-4cda-b56f-de07607d2aa5	2026-03-22 15:04:13.034051	Order {d23aab68-6342-4511-a208-ccf56c6a98a2} was updated to IN_PROGRESS	2026-03-22 14:15:56.167654	NEW	IN_PROGRESS	dcddd091-cec9-4710-8cf2-c885d1b89e1f	d23aab68-6342-4511-a208-ccf56c6a98a2
00975c0f-e481-4271-9962-290671c3cc44	2026-03-22 15:04:13.035482	Order {e3405920-c6d1-441f-a991-caed36c4d163} was updated to IN_PROGRESS	2026-03-22 14:15:56.10843	NEW	IN_PROGRESS	484b61d1-064e-4f69-be9e-9cacd5a773f6	e3405920-c6d1-441f-a991-caed36c4d163
11f05a93-1564-4df2-8f73-1509d9d6c91d	2026-03-22 15:04:13.111202	Order {a5a77d4d-b0ed-4c92-9962-fda488cd54b9} was updated to IN_PROGRESS	2026-03-22 14:10:56.158257	NEW	IN_PROGRESS	dcddd091-cec9-4710-8cf2-c885d1b89e1f	a5a77d4d-b0ed-4c92-9962-fda488cd54b9
5c05cc7b-4e88-4cf1-92a2-7f007e1047e3	2026-03-22 15:18:48.596833	Order {d23aab68-6342-4511-a208-ccf56c6a98a2} was updated to READY_FOR_PICKUP	2026-03-22 15:18:48.596833	NEW	READY_FOR_PICKUP	dcddd091-cec9-4710-8cf2-c885d1b89e1f	d23aab68-6342-4511-a208-ccf56c6a98a2
a89dce76-2d8d-49af-b32e-4c4fe212ce2a	2026-03-22 16:06:46.272326	Order {d23aab68-6342-4511-a208-ccf56c6a98a2} was updated to CANCELED	2026-03-22 14:10:56.167654	NEW	CANCELED	dcddd091-cec9-4710-8cf2-c885d1b89e1f	d23aab68-6342-4511-a208-ccf56c6a98a2
92aff492-f892-4651-8e92-efabed619fdf	2026-03-22 16:07:13.401015	Order {d23aab68-6342-4511-a208-ccf56c6a98a2} was updated to IN_PROGRESS	2026-03-22 14:10:56.167654	NEW	IN_PROGRESS	dcddd091-cec9-4710-8cf2-c885d1b89e1f	d23aab68-6342-4511-a208-ccf56c6a98a2
6f105634-1601-4bb6-8d3b-5cf19ec3aac9	2026-03-22 16:23:29.097939	Order {d23aab68-6342-4511-a208-ccf56c6a98a2} was updated to READY_FOR_PICKUP	2026-03-22 16:23:29.097939	NEW	READY_FOR_PICKUP	dcddd091-cec9-4710-8cf2-c885d1b89e1f	d23aab68-6342-4511-a208-ccf56c6a98a2
b53b3567-a7ce-450b-a246-f290932c4ccc	2026-03-22 16:23:29.108537	Order {e3405920-c6d1-441f-a991-caed36c4d163} was updated to READY_FOR_PICKUP	2026-03-22 16:23:29.108537	NEW	READY_FOR_PICKUP	484b61d1-064e-4f69-be9e-9cacd5a773f6	e3405920-c6d1-441f-a991-caed36c4d163
70b99bf1-c8a9-45a7-97bc-ac79eef49ebc	2026-03-22 16:23:29.233763	Order {a5a77d4d-b0ed-4c92-9962-fda488cd54b9} was updated to READY_FOR_PICKUP	2026-03-22 16:23:29.233763	NEW	READY_FOR_PICKUP	dcddd091-cec9-4710-8cf2-c885d1b89e1f	a5a77d4d-b0ed-4c92-9962-fda488cd54b9
\.


--
-- TOC entry 5154 (class 0 OID 24342)
-- Dependencies: 233
-- Data for Name: main_sub_product; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.main_sub_product (main_product_id, sub_product_id) FROM stdin;
88091439-2cc5-4e25-af0b-27fe6d393563	65de6f9f-c0b4-4038-896d-3bb80aa52434
88091439-2cc5-4e25-af0b-27fe6d393563	ea6e8266-b455-4136-9415-29ef8de05a15
88091439-2cc5-4e25-af0b-27fe6d393563	b9b0cc8f-53d3-40f7-9afe-d3c491ce2e75
dbcb35ab-0e7e-4af0-8517-c2ab45897f18	65de6f9f-c0b4-4038-896d-3bb80aa52434
dbcb35ab-0e7e-4af0-8517-c2ab45897f18	b9b0cc8f-53d3-40f7-9afe-d3c491ce2e75
8d3cf1a0-7b8c-450f-8cdd-6714f672027c	17c5680e-22d5-4647-b5bb-b4a586f6c6d2
8d3cf1a0-7b8c-450f-8cdd-6714f672027c	fe099c8c-06da-48b9-9c36-7df26322fe94
8d3cf1a0-7b8c-450f-8cdd-6714f672027c	8df2fa53-75b6-42c7-8798-c4157e756c41
a7dfc4a1-59f5-41d1-9cdd-c1e3ce8f0be6	17c5680e-22d5-4647-b5bb-b4a586f6c6d2
a7dfc4a1-59f5-41d1-9cdd-c1e3ce8f0be6	fe099c8c-06da-48b9-9c36-7df26322fe94
\.


--
-- TOC entry 5156 (class 0 OID 24358)
-- Dependencies: 235
-- Data for Name: product_count; Type: TABLE DATA; Schema: ffb; Owner: postgres
--

COPY ffb.product_count (id, product_count, product_id) FROM stdin;
d314e35a-0afd-429b-b9fa-a7dcfbf3627c	0	88091439-2cc5-4e25-af0b-27fe6d393563
c4088390-ccdb-47c7-99bf-1535cdf9d5ee	0	dbcb35ab-0e7e-4af0-8517-c2ab45897f18
39f0de50-b54a-4adc-8638-d7a9effc3835	0	b9b0cc8f-53d3-40f7-9afe-d3c491ce2e75
f367ea2c-7db4-4184-8eaa-2f0ec11f267b	0	ea6e8266-b455-4136-9415-29ef8de05a15
69fc2073-ffc9-48a3-87f5-f75ebc265bfb	0	8d3cf1a0-7b8c-450f-8cdd-6714f672027c
bf1718ad-871d-4357-8133-3e518e45e043	0	a7dfc4a1-59f5-41d1-9cdd-c1e3ce8f0be6
02595931-649c-412a-aba3-866dab316d40	0	17c5680e-22d5-4647-b5bb-b4a586f6c6d2
62a880f8-0afb-4fe9-b943-c90984791f35	0	8df2fa53-75b6-42c7-8798-c4157e756c41
ad1c6b4f-004d-41e0-aa3e-c32ee88f2a19	0	fe099c8c-06da-48b9-9c36-7df26322fe94
fdac0c4e-72c7-491e-b4e8-c5513ca28c11	16	65de6f9f-c0b4-4038-896d-3bb80aa52434
\.


-- Completed on 2026-03-22 17:28:17

--
-- PostgreSQL database dump complete
--

\unrestrict W6vA6DbPaSg4xpS4965itzJadQnfMK1f1O7an6IpY72vGRPD2BV0gIHFJ0HQQwH

