DROP TYPE IF EXISTS ffb.ORDER_STATUS;
CREATE TYPE ffb.ORDER_STATUS AS ENUM ('ordered', 'in_Progress', 'ready_for_Pickup', 'done', 'canceled');