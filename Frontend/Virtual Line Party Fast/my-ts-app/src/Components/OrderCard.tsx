import React from "react";
import styles from "./Modules/OrderCard.module.css";
import { Order } from "../Types";

interface OrderCardProps {
  order: Order;
  status: string;
  setClickedCard: React.Dispatch<React.SetStateAction<string>>;
}

function OrderCard(props: OrderCardProps) {
  const { order, status, setClickedCard } = props;

  const isClickable = status === "ready_for_pickup" || status === "in_progress";

  return (
    <div
      className={styles.OrderContainer}
      onClick={
        isClickable ? () => setClickedCard(order.order_number) : undefined
      }
    >
      <div className={styles.VerticalWrapper}>
        <div className={styles.FoodCourtName}>{order.foodcourt}</div>
        {status === "ready_for_pickup" && (
          <div className={styles.StatusWrapper}>
            <i className="fa-regular fa-circle-check"></i>
            <div className={styles.StatusText}>Fertig</div>
          </div>
        )}

        {status === "in_progress" && (
          <div className={styles.StatusWrapper}>
            <i className="fa-regular fa-hourglass"></i>
            <div className={styles.StatusText}>{order.waiting_time} Min</div>
          </div>
        )}

        {status === "canceled" && (
          <div className={styles.StatusWrapper}>
            <i className="fa fa-ban"></i>
            <div className={styles.StatusText}>Storniert</div>
          </div>
        )}

        {status === "done" && (
          <div className={styles.StatusWrapper}>
            <i className="fa fa-check-double"></i>
            <div className={styles.StatusText}>Abgeholt</div>
          </div>
        )}
      </div>
      <div className={styles.ItemContainer}>
        {order.orderItems.map((item, index) => (
          <div key={index} className={styles.ItemWrapper}>
            <div className={styles.FlexWrapper}>
              <div className={styles.ItemName}>{item.name}</div>
              <div>x {item.count}</div>
            </div>
            <div className={styles.ItemExtra}>{item.extra}</div>

            {item.subItems && item.subItems.length > 0 && (
              <div className={styles.SubItemContainer}>
                {item.subItems.map((subItem, subIndex) => (
                  <div key={subIndex}>
                    <div className={styles.FlexWrapper}>
                      <div>- {subItem.name}</div>
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
  );
}

export default OrderCard;
