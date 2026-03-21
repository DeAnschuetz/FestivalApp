import React, { useEffect, useState } from "react";
import styles from "./Modules/Orders.module.css";
import { Button } from "@progress/kendo-react-buttons";
import BtnBar from "../Components/BtnBar";
import OrderCard from "../Components/OrderCard";
import { Order } from "../Api/ffb/types";

interface OrderProps {
  orders: Order[];
  isFoodCourtOpen: boolean;
  setIsOrdersOpen: React.Dispatch<React.SetStateAction<boolean>>;
  setClickedCard: React.Dispatch<React.SetStateAction<string>>
}

function Orders(props: OrderProps) {
  const { orders, isFoodCourtOpen, setIsOrdersOpen, setClickedCard } = props;

  const [isOpen, setIsOpen] = useState(false);
  const [filter, setFilter] = useState<string>("");

  const showOpen = () => {
    setIsOpen(!isOpen);
    setIsOrdersOpen(!isOpen);
  };

  useEffect(() => {}, [isFoodCourtOpen]);

  const filteredOrders = orders.filter((order) => {
    if (filter === "") return true;
    return order.status === filter;
  });

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
        <div>
          <BtnBar filter={filter} setFilter={setFilter} />
          <div
            className={`${styles.OrderContainer} ${isFoodCourtOpen ? styles.BiggerContainer : ""}`}
          >
            {filteredOrders.map((order) => (
              <OrderCard order={order} status={order.status} setClickedCard={setClickedCard}/>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default Orders;
