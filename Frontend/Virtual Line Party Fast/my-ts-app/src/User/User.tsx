import React, { useEffect, useState } from "react";
import styles from "./Modules/User.module.css";
import Header from "./Header";
import { Order } from "../Types";
import ReadyforPickUp from "./ReadyforPickUp";
import InProgress from "./InProgress";

interface Props {}

function User(props: Props) {
  const {} = props;
  const [orders, setOrders] = useState<Order[]>([]);
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(
          "http://10.45.128.255:8080/food_order/list_all",
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            credentials: "include",
          },
        );

        if (!response.ok) {
          throw new Error("Failed to fetch");
        }

        const result = await response.json();
        console.log("result: ", result);
        setOrders(result);
      } catch (error) {
        console.error("Error:", error);
      }
    };

    fetchData();
  }, [token]);

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
    </div>
  );
}

export default User;
