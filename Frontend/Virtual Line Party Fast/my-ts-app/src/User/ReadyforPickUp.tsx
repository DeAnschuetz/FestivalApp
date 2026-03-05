import React from "react";
import styles from "./Modules/ReadyforPickUp.module.css";
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
          <div className={styles.OrderContainer}>
            <div className={styles.VerticalWrapper}>
              <div className={styles.FoodCourtName}>{order.foodCourtName}</div>
              <div className={styles.StatusWrapper}>
                <i className="fa-regular fa-circle-check"></i>
                <div className={styles.StatusText}>Fertig</div>
              </div>
            </div>
            <div className={styles.ItemContainer}>
              {Object.entries(
                order.orderItems.reduce(
                  (acc, item) => {
                    acc[item.displayName] = (acc[item.displayName] || 0) + 1;
                    return acc;
                  },
                  {} as Record<string, number>,
                ),
              ).map(([name, count]) => (
                <div key={name} className={styles.ItemWrapper}>
                  <div className={styles.ItemName}>{name}</div>
                  <div>x {count}</div>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ReadyforPickUp;
