import React from "react";
import styles from "./Modules/OrderCard.module.css";
import { Order } from "../Api/ffb/types";
import { FoodOrderStatus as OrderStatus} from "../Api/generated/ffbAPI.schemas";

interface OrderCardProps {
  order: Order;
  status: OrderStatus;
  setClickedCard: React.Dispatch<React.SetStateAction<string>>;
}

function OrderCard(props: OrderCardProps) {
  const { order, status, setClickedCard } = props;
  const isClickable = status === OrderStatus.READY_FOR_PICKUP || status === OrderStatus.IN_PROGRESS;

  return (
    <div
      className={styles.OrderContainer}
      onClick={
        isClickable ? () => setClickedCard(order.id) : undefined
      }
    >
      <div className={styles.VerticalWrapper}>
        <div className={styles.FoodCourtName}>{order.foodCourtName}</div>
        {status === OrderStatus.READY_FOR_PICKUP && (
          <div className={styles.StatusWrapper}>
            <i className="fa-regular fa-circle-check"></i>
            <div className={styles.StatusText}>Fertig</div>
          </div>
        )}

        {status === OrderStatus.IN_PROGRESS && (
          <div className={styles.StatusWrapper}>
            <i className="fa-regular fa-hourglass"></i>
            <div className={styles.StatusText}>{order.waitingTime} Min</div>
          </div>
        )}

        {status === OrderStatus.CANCELED && (
          <div className={styles.StatusWrapper}>
            <i className="fa fa-ban"></i>
            <div className={styles.StatusText}>Storniert</div>
          </div>
        )}

        {status === OrderStatus.DONE && (
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
              <div className={styles.ItemName}>{item.displayName}</div>
              <div>x {item.count}</div>
            </div>
            <div className={styles.ItemExtra}>{item.extra}</div>

            {item.subItems && item.subItems.length > 0 && (
              <div className={styles.SubItemContainer}>
                {item.subItems.map((subItem, subIndex) => (
                  <div key={subIndex}>
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
  );
}

export default OrderCard;
