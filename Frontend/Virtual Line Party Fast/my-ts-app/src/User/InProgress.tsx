import React from "react";
import styles from "./Modules/ReadyforPickUp.module.css";
import { Order } from "../Types";

interface Props {
  inProgress: Order[];
}

function InProgress(props: Props) {
  const { inProgress } = props;

  return (
    <div className={styles.AbholbereitContainer}>
      <div className={styles.AbholbereitTitleWrapper}>
        <i className="fa fa-check-to-slot"></i>
      </div>
      <div className={styles.OrderWrapper}>
        {inProgress.map((order) => (
          <div className={styles.OrderContainer}>
            <div className={styles.VerticalWrapper}>
              <div className={styles.FoodCourtName}>{order.foodCourtName}</div>
              <div className={styles.StatusWrapper}>
                <i className="fa-regular fa-hourglass"></i>
                <div className={styles.StatusText}>{order.waitingTime} Min</div>
              </div>
            </div>
             <div className={styles.ItemContainer}>
              {order.orderItems.map((item, index) => (
                <div key={index} className={styles.ItemWrapper}>
                  <div className={styles.FlexWrapper}>
                    <div className={styles.ItemName}>{item.displayName}</div>
                    <div>x {item.count}</div>
                  </div>
                  <div className={styles.ItemExtra}>{item.extra}</div>

                  {item.subItems && item.subItems.length > 0 && (
                    <div className={styles.SubItemContainer}>
                      {item.subItems.map((subItem, subIndex) => (
                        <div key={subIndex} >
                          <div className={styles.FlexWrapper}>
                            <div>- {subItem.displayName}</div>
                            <div> x {subItem.count}</div>
                          </div>
                          <div className={styles.Extra}>{subItem.extra}</div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default InProgress;
