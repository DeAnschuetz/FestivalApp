import React, { useState } from "react";
import styles from "./Modules/Orders.module.css";
import { Button } from "@progress/kendo-react-buttons";
import BtnBar from "../Components/BtnBar";
import { Order } from "../Api/ffb/types";

interface Props {
  orders: Order[];
}

function Orders(props: Props) {
  const { orders } = props;
  console.log("orders: ", orders);

  const [isOpen, setIsOpen] = useState(false);

  const showOpen = () => {
    setIsOpen(!isOpen);
  };

  return (
    <div>
      <div className={styles.Header}>
        <Button
          type="button"
          fillMode={"flat"}
          iconClass={!isOpen ? "fa fa-angle-left" : "fa fa-angle-down"}
          size={"medium"}
          onClick={showOpen}
        ></Button>
        <div
          className={`${styles.BtnLabel}  ${isOpen ? styles.OpenLabel : ""}`}
        >
          Orders
        </div>
      </div>
      {isOpen && (
        <div className={styles.Content}>
          <BtnBar />
        </div>
      )}
    </div>
  );
}

export default Orders;
