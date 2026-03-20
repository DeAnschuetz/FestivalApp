import React from "react";
import styles from "./Modules/ReadyforPickUp.module.css";
import OrderCard from "../Components/OrderCard";
import { Order } from "../Types";

interface InProgressProps {
  inProgress: Order[];
}

function InProgress(props: InProgressProps) {
  const { inProgress } = props;

  return (
    <div className={styles.AbholbereitContainer}>
      <div className={styles.AbholbereitTitleWrapper}>
        <i className="fa fa-check-to-slot"></i>
      </div>
      <div className={styles.OrderWrapper}>
        {inProgress.map((order) => (
          <OrderCard order={order} status="in_progress" />
        ))}
      </div>
    </div>
  );
}

export default InProgress;
