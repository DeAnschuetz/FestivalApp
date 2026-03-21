import React, { useState } from "react";
import styles from "./Modules/ViewOrder.module.css";
import { Button } from "@progress/kendo-react-buttons";
import { QRCodeCanvas } from "qrcode.react";
import ShareOrderDialog from "./ShareOrderDialog";
import { Order } from "../Api/ffb/types";

interface ViewOrderProps {
  currentOrder?: Order;
  setClickedCard: React.Dispatch<React.SetStateAction<string>>;
}

function ViewOrder({ currentOrder, setClickedCard }: ViewOrderProps) {
  const [openShareDialog, setOpenShareDialog] = useState(false);

  return (
    <div className={styles.ViewOrder}>
      <div className={styles.HeaderContainer}>
        <div>Order {currentOrder?.id}</div>
        <Button
          type="button"
          fillMode="flat"
          iconClass="fa-regular fa-circle-xmark"
          size="small"
          style={{ fontSize: "24px" }}
          onClick={() => setClickedCard("")}
        />
      </div>

      <div className={styles.OrderBody}>
        <div className={styles.OrderContainer}>
          {currentOrder?.orderItems?.map((item, index) => (
            <div className={styles.ItemWrapper}>
              <div key={index} className={styles.OrderItem}>
                <div>{item.displayName}</div>
                <div className={styles.Extra}>{item.extra}</div>
              </div>
              <div>x {item.count}</div>
            </div>
          ))}
        </div>

        <div className={styles.FlexWrapper}>
          <div className={styles.WaitingTime}>
            <i className="fa-regular fa-clock"></i>
            <div className={styles.TimeText}>
              {currentOrder?.waitingTime} Min
            </div>
          </div>
          <Button
            iconClass="fa fa-share"
            className={styles.ShareBtn}
            onClick={() => setOpenShareDialog(true)}
          >
            Bestellung Freigeben
          </Button>
        </div>
        <div>
          {currentOrder && (
            <QRCodeCanvas value={currentOrder.id} size={220} />
          )}
        </div>
      </div>

      {openShareDialog && <ShareOrderDialog setOpenShareDialog={setOpenShareDialog}/>}
    </div>
  );
}

export default ViewOrder;
