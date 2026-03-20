import styles from "./Modules/ReadyforPickUp.module.css";
import { Order } from "../Api/ffb/types";

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

export default ReadyforPickUp;
