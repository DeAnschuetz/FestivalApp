import React from "react";
import styles from "./Modules/ReadyforPickUp.module.css";
import OrderCard from "../Components/OrderCard";
import { Order } from "../Api/ffb/types";

interface InProgressProps {
  inProgress: Order[];
  setClickedCard: React.Dispatch<React.SetStateAction<string>>
}

function InProgress(props: InProgressProps) {
  const { inProgress, setClickedCard } = props;

  return (
    <div className={styles.AbholbereitContainer}>
      <div className={styles.AbholbereitTitleWrapper}>
        <i className="fa fa-check-to-slot"></i>
      </div>
      <div className={styles.OrderWrapper}>
        {inProgress.map((order) => (
          <OrderCard order={order} status="in_progress" setClickedCard={setClickedCard}/>
        ))}
      </div>
    </div>
  );
}

export default InProgress;
