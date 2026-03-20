import React from "react";
import styles from "./Modules/ReadyforPickUp.module.css";
import OrderCard from "../Components/OrderCard";
import { Order } from "../Types";

interface Props {
  readyForPickup: Order[];
}

function ReadyforPickUp(props: Props) {
  const { readyForPickup } = props;

  return (
    <div className={styles.AbholbereitContainer}>
      <div className={styles.AbholbereitTitleWrapper}>
        <i className="fa fa-clipboard-list"></i>
        <div className={styles.AbholbereitTitle}>Abholbereit</div>
      </div>
      <div className={styles.OrderWrapper}>
        {readyForPickup.map((order) => (
          <OrderCard order={order} status="ready_for_pickup"/>
        ))}
      </div>
    </div>
  );
}

export default ReadyforPickUp;
