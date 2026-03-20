import React, { useEffect, useState } from "react";
import styles from "./Modules/User.module.css";
import Header from "./Header";
import ReadyforPickUp from "./ReadyforPickUp";
import InProgress from "./InProgress";
import Orders from "./Orders";
import FoodCourt from "./FoodCourt";
import { getVisibleOrders } from "../Api/ffb";
import { Order } from "../Api/ffb/types"

interface Props {}

function User(props: Props) {
  const {} = props;
  const [orders, setOrders] = useState<Order[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data: Order[] = await getVisibleOrders();

        console.log("Order data: ", data);
        setOrders(data);
      } catch (error) {
        console.error("Error:", error);
      }
    };

    fetchData();
  }, []);

  const readyForPickup = orders.filter(
    (order: Order) => order.status === "READY_FOR_PICKUP",
  );

  const inProgress = orders.filter(
    (order: Order) => order.status === "IN_PROGRESS",
  );

  return (
    <div className={styles.Container}>
      <Header />
      <ReadyforPickUp readyForPickup={readyForPickup} />
      <InProgress inProgress={inProgress}/>
      <Orders orders={orders}/>
      <FoodCourt/>
    </div>
  );
}

export default User;
