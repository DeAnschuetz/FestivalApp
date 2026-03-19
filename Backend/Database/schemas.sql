--
-- PostgreSQL database dump
--

\restrict ucT2U4NpsVq9SLyuoWCCcxO2SQu0XG1MmbjPIkl83vAXbeJWIOURybf6dy6Z4ae

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

-- Started on 2026-03-19 21:43:22

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
-- TOC entry 7 (class 2615 OID 18483)
-- Name: ffb; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA ffb;


ALTER SCHEMA ffb OWNER TO postgres;

--
-- TOC entry 8 (class 2615 OID 21919)
-- Name: virtual; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA virtual;


ALTER SCHEMA virtual OWNER TO postgres;

--
-- TOC entry 2 (class 3079 OID 17078)
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- TOC entry 5148 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 222 (class 1259 OID 23995)
-- Name: account; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.account (
    id uuid NOT NULL,
    password character varying(60) NOT NULL,
    type character varying(255) NOT NULL,
    ticket_id uuid NOT NULL,
    CONSTRAINT account_type_check CHECK (((type)::text = ANY ((ARRAY['ADMIN'::character varying, 'FOOD_COURT_WORKER'::character varying, 'GUEST'::character varying])::text[])))
);


ALTER TABLE ffb.account OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 24005)
-- Name: cart; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.cart (
    id uuid NOT NULL,
    has_prio boolean,
    total numeric(17,2),
    account_id uuid NOT NULL
);


ALTER TABLE ffb.cart OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 24012)
-- Name: cart_item; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.cart_item (
    id uuid NOT NULL,
    extra character varying(255),
    item_count integer,
    price numeric(17,2),
    cart_id uuid NOT NULL,
    product_id uuid NOT NULL
);


ALTER TABLE ffb.cart_item OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 24020)
-- Name: credit; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.credit (
    id uuid NOT NULL,
    amount numeric(17,2),
    account_id uuid NOT NULL
);


ALTER TABLE ffb.credit OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 24027)
-- Name: credit_history; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.credit_history (
    id uuid NOT NULL,
    change_time timestamp(6) without time zone,
    new_amount numeric(17,2),
    old_amount numeric(17,2),
    credit_id uuid NOT NULL
);


ALTER TABLE ffb.credit_history OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 24034)
-- Name: food_court; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.food_court (
    id uuid NOT NULL,
    display_name character varying(100) NOT NULL,
    image oid,
    account_id uuid NOT NULL
);


ALTER TABLE ffb.food_court OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 24042)
-- Name: food_court_waiting_time; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.food_court_waiting_time (
    id uuid NOT NULL,
    waiting_time integer DEFAULT 0 NOT NULL,
    food_court_id uuid NOT NULL,
    CONSTRAINT food_court_waiting_time_waiting_time_check CHECK ((waiting_time >= 0))
);


ALTER TABLE ffb.food_court_waiting_time OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 24052)
-- Name: food_order; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.food_order (
    id uuid NOT NULL,
    has_prio boolean,
    is_hidden boolean,
    order_time timestamp(6) without time zone,
    status character varying(255),
    total numeric(17,2),
    waiting_time integer,
    account_id uuid NOT NULL,
    food_court_id uuid NOT NULL,
    shared_account_id uuid,
    CONSTRAINT food_order_status_check CHECK (((status)::text = ANY ((ARRAY['ORDERED'::character varying, 'IN_PROGRESS'::character varying, 'READY_FOR_PICKUP'::character varying, 'DONE'::character varying, 'CANCELED'::character varying])::text[])))
);


ALTER TABLE ffb.food_order OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 24061)
-- Name: food_order_history; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.food_order_history (
    id uuid NOT NULL,
    new_status character varying(255),
    old_status character varying(255),
    status_change_time timestamp(6) without time zone,
    food_order_id uuid NOT NULL,
    CONSTRAINT food_order_history_new_status_check CHECK (((new_status)::text = ANY ((ARRAY['ORDERED'::character varying, 'IN_PROGRESS'::character varying, 'READY_FOR_PICKUP'::character varying, 'DONE'::character varying, 'CANCELED'::character varying])::text[]))),
    CONSTRAINT food_order_history_old_status_check CHECK (((old_status)::text = ANY ((ARRAY['ORDERED'::character varying, 'IN_PROGRESS'::character varying, 'READY_FOR_PICKUP'::character varying, 'DONE'::character varying, 'CANCELED'::character varying])::text[])))
);


ALTER TABLE ffb.food_order_history OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 24072)
-- Name: food_order_item; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.food_order_item (
    id uuid NOT NULL,
    extra character varying(255),
    item_count integer,
    price numeric(17,2),
    food_order_id uuid NOT NULL,
    product_id uuid NOT NULL
);


ALTER TABLE ffb.food_order_item OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 24080)
-- Name: food_order_notification; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.food_order_notification (
    id uuid NOT NULL,
    creation_time timestamp(6) without time zone,
    message character varying(255),
    pickup_time timestamp(6) without time zone,
    status character varying(255),
    type character varying(255),
    account_id uuid NOT NULL,
    order_id uuid NOT NULL,
    CONSTRAINT food_order_notification_status_check CHECK (((status)::text = ANY ((ARRAY['NEW'::character varying, 'READ'::character varying, 'REMOVED'::character varying])::text[]))),
    CONSTRAINT food_order_notification_type_check CHECK (((type)::text = ANY ((ARRAY['ORDERED'::character varying, 'IN_PROGRESS'::character varying, 'READY_FOR_PICKUP'::character varying, 'DONE'::character varying, 'CANCELED'::character varying])::text[])))
);


ALTER TABLE ffb.food_order_notification OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 24092)
-- Name: main_sub_product; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.main_sub_product (
    main_product_id uuid NOT NULL,
    sub_product_id uuid NOT NULL
);


ALTER TABLE ffb.main_sub_product OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 24099)
-- Name: product; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.product (
    id uuid NOT NULL,
    display_name character varying(100) NOT NULL,
    minimal_warning integer,
    price numeric(10,2),
    symbol_identifier character varying(100) NOT NULL,
    food_court_id uuid NOT NULL
);


ALTER TABLE ffb.product OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 24108)
-- Name: product_count; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.product_count (
    id uuid NOT NULL,
    product_count integer,
    product_id uuid NOT NULL
);


ALTER TABLE ffb.product_count OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 24115)
-- Name: ticket; Type: TABLE; Schema: ffb; Owner: postgres
--

CREATE TABLE ffb.ticket (
    id uuid NOT NULL,
    login_nr character varying(13) NOT NULL
);


ALTER TABLE ffb.ticket OWNER TO postgres;

--
-- TOC entry 4933 (class 2606 OID 24004)
-- Name: account account_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


--
-- TOC entry 4941 (class 2606 OID 24019)
-- Name: cart_item cart_item_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.cart_item
    ADD CONSTRAINT cart_item_pkey PRIMARY KEY (id);


--
-- TOC entry 4937 (class 2606 OID 24011)
-- Name: cart cart_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.cart
    ADD CONSTRAINT cart_pkey PRIMARY KEY (id);


--
-- TOC entry 4947 (class 2606 OID 24033)
-- Name: credit_history credit_history_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.credit_history
    ADD CONSTRAINT credit_history_pkey PRIMARY KEY (id);


--
-- TOC entry 4943 (class 2606 OID 24026)
-- Name: credit credit_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.credit
    ADD CONSTRAINT credit_pkey PRIMARY KEY (id);


--
-- TOC entry 4949 (class 2606 OID 24041)
-- Name: food_court food_court_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_court
    ADD CONSTRAINT food_court_pkey PRIMARY KEY (id);


--
-- TOC entry 4953 (class 2606 OID 24051)
-- Name: food_court_waiting_time food_court_waiting_time_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_court_waiting_time
    ADD CONSTRAINT food_court_waiting_time_pkey PRIMARY KEY (id);


--
-- TOC entry 4959 (class 2606 OID 24071)
-- Name: food_order_history food_order_history_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_order_history
    ADD CONSTRAINT food_order_history_pkey PRIMARY KEY (id);


--
-- TOC entry 4961 (class 2606 OID 24079)
-- Name: food_order_item food_order_item_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_order_item
    ADD CONSTRAINT food_order_item_pkey PRIMARY KEY (id);


--
-- TOC entry 4963 (class 2606 OID 24091)
-- Name: food_order_notification food_order_notification_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_order_notification
    ADD CONSTRAINT food_order_notification_pkey PRIMARY KEY (id);


--
-- TOC entry 4957 (class 2606 OID 24060)
-- Name: food_order food_order_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_order
    ADD CONSTRAINT food_order_pkey PRIMARY KEY (id);


--
-- TOC entry 4965 (class 2606 OID 24098)
-- Name: main_sub_product main_sub_product_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.main_sub_product
    ADD CONSTRAINT main_sub_product_pkey PRIMARY KEY (main_product_id, sub_product_id);


--
-- TOC entry 4969 (class 2606 OID 24114)
-- Name: product_count product_count_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.product_count
    ADD CONSTRAINT product_count_pkey PRIMARY KEY (id);


--
-- TOC entry 4967 (class 2606 OID 24107)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- TOC entry 4973 (class 2606 OID 24121)
-- Name: ticket ticket_pkey; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.ticket
    ADD CONSTRAINT ticket_pkey PRIMARY KEY (id);


--
-- TOC entry 4945 (class 2606 OID 24127)
-- Name: credit uk2s9bu4qkeqw92ljwog2l2jbhc; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.credit
    ADD CONSTRAINT uk2s9bu4qkeqw92ljwog2l2jbhc UNIQUE (account_id);


--
-- TOC entry 4951 (class 2606 OID 24129)
-- Name: food_court ukd30k9lhuhcre6n20b0xt1oxaw; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_court
    ADD CONSTRAINT ukd30k9lhuhcre6n20b0xt1oxaw UNIQUE (account_id);


--
-- TOC entry 4935 (class 2606 OID 24123)
-- Name: account ukmq7ttihv7bufliy9xfp5k66u3; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.account
    ADD CONSTRAINT ukmq7ttihv7bufliy9xfp5k66u3 UNIQUE (ticket_id);


--
-- TOC entry 4975 (class 2606 OID 24135)
-- Name: ticket ukno93itpmkv2dkq2s91c3v3odg; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.ticket
    ADD CONSTRAINT ukno93itpmkv2dkq2s91c3v3odg UNIQUE (login_nr);


--
-- TOC entry 4939 (class 2606 OID 24125)
-- Name: cart ukoem6haiy2c42obpy8m5ye4brj; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.cart
    ADD CONSTRAINT ukoem6haiy2c42obpy8m5ye4brj UNIQUE (account_id);


--
-- TOC entry 4955 (class 2606 OID 24131)
-- Name: food_court_waiting_time ukp9bks0wdd0l97qvwtfmspyeo4; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_court_waiting_time
    ADD CONSTRAINT ukp9bks0wdd0l97qvwtfmspyeo4 UNIQUE (food_court_id);


--
-- TOC entry 4971 (class 2606 OID 24133)
-- Name: product_count ukpkfst54yvr9rtrbrhr20rxjwl; Type: CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.product_count
    ADD CONSTRAINT ukpkfst54yvr9rtrbrhr20rxjwl UNIQUE (product_id);


--
-- TOC entry 4977 (class 2606 OID 24141)
-- Name: cart fk_account; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.cart
    ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES ffb.account(id);


--
-- TOC entry 4980 (class 2606 OID 24156)
-- Name: credit fk_account; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.credit
    ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES ffb.account(id);


--
-- TOC entry 4982 (class 2606 OID 24166)
-- Name: food_court fk_account; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_court
    ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES ffb.account(id);


--
-- TOC entry 4984 (class 2606 OID 24176)
-- Name: food_order fk_account; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_order
    ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES ffb.account(id);


--
-- TOC entry 4990 (class 2606 OID 24206)
-- Name: food_order_notification fk_account; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_order_notification
    ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES ffb.account(id);


--
-- TOC entry 4978 (class 2606 OID 24146)
-- Name: cart_item fk_cart; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.cart_item
    ADD CONSTRAINT fk_cart FOREIGN KEY (cart_id) REFERENCES ffb.cart(id);


--
-- TOC entry 4981 (class 2606 OID 24161)
-- Name: credit_history fk_credit; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.credit_history
    ADD CONSTRAINT fk_credit FOREIGN KEY (credit_id) REFERENCES ffb.credit(id);


--
-- TOC entry 4983 (class 2606 OID 24171)
-- Name: food_court_waiting_time fk_food_court; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_court_waiting_time
    ADD CONSTRAINT fk_food_court FOREIGN KEY (food_court_id) REFERENCES ffb.food_court(id);


--
-- TOC entry 4985 (class 2606 OID 24181)
-- Name: food_order fk_food_court; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_order
    ADD CONSTRAINT fk_food_court FOREIGN KEY (food_court_id) REFERENCES ffb.food_court(id);


--
-- TOC entry 4994 (class 2606 OID 24226)
-- Name: product fk_food_court; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.product
    ADD CONSTRAINT fk_food_court FOREIGN KEY (food_court_id) REFERENCES ffb.food_court(id);


--
-- TOC entry 4987 (class 2606 OID 24191)
-- Name: food_order_history fk_food_order; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_order_history
    ADD CONSTRAINT fk_food_order FOREIGN KEY (food_order_id) REFERENCES ffb.food_order(id);


--
-- TOC entry 4988 (class 2606 OID 24196)
-- Name: food_order_item fk_food_order; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_order_item
    ADD CONSTRAINT fk_food_order FOREIGN KEY (food_order_id) REFERENCES ffb.food_order(id);


--
-- TOC entry 4991 (class 2606 OID 24211)
-- Name: food_order_notification fk_food_order; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_order_notification
    ADD CONSTRAINT fk_food_order FOREIGN KEY (order_id) REFERENCES ffb.food_order(id);


--
-- TOC entry 4992 (class 2606 OID 24216)
-- Name: main_sub_product fk_main_product; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.main_sub_product
    ADD CONSTRAINT fk_main_product FOREIGN KEY (main_product_id) REFERENCES ffb.product(id);


--
-- TOC entry 4979 (class 2606 OID 24151)
-- Name: cart_item fk_product; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.cart_item
    ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES ffb.product(id);


--
-- TOC entry 4989 (class 2606 OID 24201)
-- Name: food_order_item fk_product; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_order_item
    ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES ffb.product(id);


--
-- TOC entry 4995 (class 2606 OID 24231)
-- Name: product_count fk_product; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.product_count
    ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES ffb.product(id);


--
-- TOC entry 4986 (class 2606 OID 24186)
-- Name: food_order fk_shared_account; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.food_order
    ADD CONSTRAINT fk_shared_account FOREIGN KEY (shared_account_id) REFERENCES ffb.account(id);


--
-- TOC entry 4993 (class 2606 OID 24221)
-- Name: main_sub_product fk_sub_product; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.main_sub_product
    ADD CONSTRAINT fk_sub_product FOREIGN KEY (sub_product_id) REFERENCES ffb.product(id);


--
-- TOC entry 4976 (class 2606 OID 24136)
-- Name: account fk_ticket; Type: FK CONSTRAINT; Schema: ffb; Owner: postgres
--

ALTER TABLE ONLY ffb.account
    ADD CONSTRAINT fk_ticket FOREIGN KEY (ticket_id) REFERENCES ffb.ticket(id);


-- Completed on 2026-03-19 21:43:22

--
-- PostgreSQL database dump complete
--

\unrestrict ucT2U4NpsVq9SLyuoWCCcxO2SQu0XG1MmbjPIkl83vAXbeJWIOURybf6dy6Z4ae

